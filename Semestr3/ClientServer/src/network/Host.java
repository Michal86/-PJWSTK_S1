package network;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.*;


public class Host implements Runnable {

    private Utility                 util;
    private final String            ADDRESS = "localhost";
    private boolean isClientLocal = true;   //true = server.accept side; false = outside
    private Server                  server;
    private Socket                  clientSocket;
    private DataOutputStream        out;
    private DataInputStream         in;
    private Map<String, String>     connectedToServerFiles = new HashMap<>();
    private List<String>            serverFileListName = new ArrayList<>();
    private String                  clientName;
    private String                  connectedTo;
    private int                     port;
    private String                  forLogClientName;
    //-- MULTI --
    private volatile int            mySplitNumber = 1;
    private volatile boolean        resumeRequired = false;
    private FileTransfer            toFinish = null;
    private String                  fileNameToResume;

    //==============================================
    //=== THIS IS CONNECTION TO REMOTE SERVER SIDE ===
    public Host(Server server, String connectionName) {
        this.server = server;
        this.clientName = server.getServerName();
        //Connection
        NameToPort tmp = NameToPort.valueOf(connectionName.toUpperCase());
        connectedTo = tmp.name();
        port = tmp.getPort();
        isClientLocal = false;
        util = new Utility();
    }
    //=== THIS IS FOR SERVER CONNECTION HANDLER ===
    public Host(Socket client, Server server) {
        this.clientSocket = client;
        this.server = server;
        util = new Utility();
    }
    //==============================================

    private void handshakeCase(boolean typeOfConnection) throws Exception{
        boolean done = false;

        if (!typeOfConnection){ //remote
            connectionMsg();
            String msgFromServer = in.readUTF();
            server.queueMessage(msgFromServer);

            while (!done) {
                out.writeUTF("get");
                out.flush();
                done = receiveServerFiles(in);

                if (done){
                    out.writeUTF("done");
                    out.flush();
                }
            }
            server.queueMessage("#> Handshake done, list with files downloaded.");
        }
        else {  //INCOMING -> send available files
            connectedToServerFiles = server.getMyServerFiles(); //check and collect MyFiles
            server.queueMessage(getClientConnectionMsg());
            sendServerWelcome();
            while (!done) {
                String message = in.readUTF();
                if (message.equals("get")){
                    sendFileList(connectedToServerFiles);   //Send list of my files
                }
                message = in.readUTF();
                if (message.equals("done"))
                    done = true;
            }
            server.queueMessage("--- File list sent ---");
        }
    }
    //====== RUN ======
    public void run() {
        try {
            if (!isClientLocal) {
                clientSocket = new Socket(ADDRESS, port);
                //CHECK IF THE HOST WAS DISCONNECTED WHILE DOWNLOADING
                toFinish = server.getStoppedFileTransfer(connectedTo);
                if (toFinish != null)
                    setResumeRequired(true);
            }
            out = new DataOutputStream(clientSocket.getOutputStream());
            in  = new DataInputStream(clientSocket.getInputStream());

            //--- GREETINGS ---
            handshakeCase(isClientLocal);

            //--- RESUME DOWNLOAD IF REQUIRED ---
            if(isResumeRequired()){
                server.setServerMaxSplits(toFinish.getTotalSplits());
                server.queueMessage("#> -> Auto request <-\n"+
                        "Get: "+ toFinish.getFileName() +"\nFrom "+ toFinish.getHostName()+
                        " ("+ toFinish.getWhereStopped()/1024 +"KB to "+ toFinish.getLength()/1024+"KB)");
                //===================================
                System.out.println("HOST: "+toFinish.getHostName() + "\n" +
                        "FILE: "+toFinish.getFileName() + "\n" +
                        "START: "+toFinish.getWhereStopped() + " / " +
                        "FINISH: "+toFinish.getLength() + "\n" +
                        "SPLIT: "+toFinish.getMySplit() + "\n" +
                        "PARTS: "+toFinish.getTotalSplits());

                //===================================
                sendCommand(7, toFinish.getFileName());
                out.writeLong(toFinish.getLength());
                out.writeLong(toFinish.getWhereStopped());

            }
            //-----------------
            String received = "";
            while (!received.equals("logout") ) { //&& clientSocket.isConnected()
                received = in.readUTF();
                if (!received.equals("logout"))
                    checkCommand(received);
            }
            //---------------------------------
            server.checkClientsConnections();
            if (!isClientLocal)
                server.removeDisconnected(this);
            else
                server.checkClientsConnections();

            server.queueMessage("--- "+ getIncomingHostName() + " logout ---");
        }
        catch (ConnectException ce){
            server.queueMessage("--- Connection lost ---");
        }
        catch (IOException io){
            System.out.println(io.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            server.removeDisconnected(this);
            this.stopConnection();
        }
    }
    //=== REQUESTS ====
    private void checkCommand(String request) throws Exception {
        boolean noErrors;
        String info;
        int myPart,allParts;
        //--- Extract request number ---
        int messageType = Integer.parseInt(request.substring(0,1));
        String file = request.substring(1);
        saveDownloadingFileName(file);
        //--- prepare file path ---
        String filePath = server.getFolderPath()+"/"+file;

        switch(messageType)
        {
            case 1: // request file -> send
                server.queueMessage("Sending : " + file);
                sendCommand(2, file);
                objSend(filePath);
                server.queueMessage("--- File sent ---");
                break;
            case 2: // get -> receive file
                objRead(file);
                break;
            case 3: // prepare path
                server.queueMessage("Receiving file: " + file);
                sendCommand(4, file);
                objRead(file);
                server.queueMessage("--- File received ---");
                break;
            case 4: // push
                objSend(server.getFolderPath()+"/"+file);
                server.queueMessage("--- File pushed ---");
                break;
            case 5: // multi download
                info = in.readUTF();
                myPart = Integer.parseInt(info.substring(0,1)); //FOR SIMPLICITY MAX IS 3
                allParts = Integer.parseInt(info.substring(1,2)); //MAX 3, NO DOUBLE DIGITS
                server.queueMessage("--- Sending: " + filePath + " ---\n Part "+myPart+ " of "+allParts);

                sendCommand(6, file);   //answer
                noErrors = util.sendFilePiece(out, filePath, myPart, allParts);

                if (noErrors){
                    server.queueMessage("--- Part "+myPart+ " of "+filePath+" sent ---");
                } else
                    server.queueMessage("--- Interrupted part "+myPart+ " of "+filePath+" ---");
                break;
            case 6: // GET -> File piece
                if (toFinish == null) {
                    server.queueMessage("Getting (" + connectedTo + ") part "+getMySplitNumber()+" of file: " + file);
                    noErrors = downloadFile(file, getMySplitNumber());

                    if (noErrors) server.setFinishedPart(file, false);
                }
                else { // GET -> RESUME downloading
                    if (toFinish.getTotalSplits()>1)
                        noErrors = downloadFile(file, toFinish.getTotalSplits()); //multiple parts
                    else
                        noErrors = downloadFile(file, toFinish.getTotalSplits()-1); //single file

                    if (noErrors) {
                        server.removeFinishedTransfer(toFinish);

                        if (toFinish.getTotalSplits()>1)
                            server.setFinishedPart(file, true);
                        else
                            server.queueMessage("--- Auto resume completed --- \n" +
                                "#> Checking MD5 of new file from "+connectedTo+
                                    "\nMD5 ["+ server.getMD5(new File(filePath))+"]");
                    }

                }
                break;
            case 7: // RESUME -> send bytes
                long remainingBytes = in.readLong();
                long start = in.readLong();
                server.queueMessage("--- Resuming: " + filePath +
                        " ---\n("+start/1024+"/"+remainingBytes/1024+") KB ");

                sendCommand(6, file);
                out.flush();
                if(util.sendFilePiece(out, filePath, start, remainingBytes))
                    server.queueMessage("--- File sent ---");
                System.out.println("SEND ALL");
                break;
            case 8:
                server.queueMessage("--- New file(s) available [" + file+"] ---"); //file here stands for host
                break;
            case 9:
                server.queueMessage("--- File(s) removed from the list [" + file+"] ---"); //file here stands for host
                break;
        }
    }

    private void saveDownloadingFileName(String file) {
        this.fileNameToResume = file;
    }

    void sendCommand(int type, String name){
        try {
            out.writeUTF(type+name);
            out.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void objSend(String initialFile){
        try {
            util.sendFilePiece(out, initialFile, 1, 1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void objRead(String initialFile){
        try {
            boolean noErrors = downloadFile(initialFile, 0);
            if (noErrors)
                server.queueMessage("--- Downloaded ---");
        }
        catch (Exception e){
            server.queueMessage("--- Interrupted ---");
        }
    }

    //====== CLIENT SIDE ======
    private void connectionMsg() throws IOException {
        String sendMessage = clientName;
        out.writeUTF(sendMessage);
        out.flush();
    }

    private boolean receiveServerFiles(DataInputStream din) throws Exception{
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        util.getBytes(din, byteOut);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream oin = new ObjectInputStream(byteIn);
        byteOut.close();
        byteIn.close();
        Map<String, String> tmpItem = (Map<String, String>) oin.readObject();
        oin.close();
        connectedToServerFiles.putAll(tmpItem);
        tmpItem.clear();

        updateFileListOnChange();

        return connectedToServerFiles.size() > 0;
    }

    private void updateFileListOnChange(){
        connectedToServerFiles.keySet().stream().filter( e -> !serverFileListName.contains(e))
                .forEach(f ->serverFileListName.add(f));
    }

    private boolean downloadFile(String name, int splitNumber) {
        boolean append;
        BufferedOutputStream bos = null;

        try {
            if (splitNumber > 0) {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        server.getFolderPath() + "/split." + splitNumber), true));
            } else {
                append = toFinish != null;
                bos = new BufferedOutputStream(new FileOutputStream(
                        server.getFolderPath() + "/" + name, append));
            }
            this.util.getBytes(this.in, bos);

            bos.close();
            if (splitNumber == 0) server.updateFileList(name);

            return true;
        } catch (IOException io) {
            getBrokenFileStatus(name);
            try {
                bos.close();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            getBrokenFileStatus(name);
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private void getBrokenFileStatus(String name){
        FileTransfer save = prepareFileTransferSave(toFinish, name);

//        System.out.println("HOST: "+save.getHostName() + "\n" +
//                "FILE: "+save.getFileName() + "\n" +
//                "START: "+save.getWhereStopped() + " / " +
//                "FINISH: "+save.getLength() + "\n" +
//                "SPLIT: "+save.getMySplit() + "\n" +
//                "PARTS: "+save.getTotalSplits());

        server.addStoppedTransfer(save);
    }

    private FileTransfer prepareFileTransferSave(FileTransfer exist, String fileName) {
        int splitNo, totalSplits;

        if(exist == null) {
            splitNo = getMySplitNumber();
            totalSplits = server.getServerMaxSplits();
        } else {
            splitNo = exist.getMySplit();
            totalSplits = exist.getTotalSplits();
        }

        return new FileTransfer(getConnectedTo(), fileName, util.getReadAmount(), util.getPartSize(), splitNo, totalSplits);
    }

    //====== SERVER SIDE ======
    private String getClientConnectionMsg() throws IOException{
        String msgFromClient = in.readUTF();
        String[] parts = msgFromClient.split(" ", 2);
        setIncomingHostName(parts[0]);
        String newMessage = "Connection #" + server.getCounter() +
                "> from " + msgFromClient;

        return newMessage;
    }

    private void sendServerWelcome() throws IOException{
        String sendMessage = "Connection with " + server.getServerName() +" established.";
        out.writeUTF(sendMessage);
        out.flush();
    }

    private void sendFileList(Map<String, String> fileList) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteOut);
        oos.writeObject(fileList);
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        oos.close();
        byteOut.close();

        util.sendBytes(byteIn, this.out);
        byteIn.close();
    }

    //SETTERS GETTERS
    synchronized void setSplitNumber(int number){ //SET BY handler
        this.mySplitNumber = number;
    }

    private int getMySplitNumber(){
        return this.mySplitNumber;
    }

    private void setResumeRequired(boolean resumeRequired) {
        this.resumeRequired = resumeRequired;
    }

    private boolean isResumeRequired() {
        return resumeRequired;
    }

    public void stopConnection() {
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (out != null) {
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (in != null) {
            try {
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public List<String> getServerFileList() {
        return serverFileListName;
    }

    public int getSizeOfFileList() {
        return connectedToServerFiles.size();
    }

    public Map<String, String> getConnectedToServerFiles() {
        return connectedToServerFiles;
    }

    public String getConnectedTo() {
        return connectedTo;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public DataInputStream getIn() {
        return in;
    }

    private void setIncomingHostName(String name){
        this.forLogClientName = name;
    }

    private String getIncomingHostName(){
        return this.forLogClientName;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public boolean isClientLocal() {
        return isClientLocal;
    }

    public synchronized void setClientLocal(boolean clientLocal) {
        isClientLocal = clientLocal;
    }

}
