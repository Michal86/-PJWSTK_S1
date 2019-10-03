package network;

import javafx.collections.ObservableList;

import java.io.*;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Server extends Thread{

    private final static String     DIR_PATH = "D:\\TORrent_";
    private boolean                 isServerRunning = true;
    private String                  folderPath;
    private String                  serverName;
    private ServerSocket            server;
    private int                     myPort;
    private volatile int            count = 0;
    //LISTS: files & connections
    private Map<String, String>     myFileList;
    private List<String>            fileNamesList;
    private List<Host>              clientHandlerList; //incoming connections
    private List<Host>              myConnections;
    private volatile String         pushFile;
    //MULTI DOWNLOAD & RESUME
    private int                     serverMaxSplits = 1;
    private volatile int            finishedParts = 0;
    private List<FileTransfer>      stoppedTransfers;
    //OBSERVERS
    private final List<RemoteConnectionListener> listeners = new ArrayList<>();
    private final List<UpdateServerLogListener> logListeners = new ArrayList<>();
    private final ConcurrentLinkedQueue<String> messagesQueue = new ConcurrentLinkedQueue<>();

    //=============================================
    public Server(String serverName){
        NameToPort tmp = NameToPort.valueOf(serverName.toUpperCase());
        this.serverName = tmp.name();
        this.myPort = tmp.getPort();
        this.folderPath = DIR_PATH + tmp.getFolderCode();
        //---
        myFileList = new HashMap<>();
        fileNamesList = new ArrayList<>();
        clientHandlerList = new LinkedList<>();
        myConnections = new LinkedList<>();
        stoppedTransfers = new CopyOnWriteArrayList<>();
    }
    //=============================================

    //--- Listeners: log updates & connection changes ---
    void addListener(RemoteConnectionListener listener){
        listeners.add(listener);
    }

    void addLogListener(UpdateServerLogListener logListener){
        logListeners.add(logListener);
    }
    //---------------------------------------------------

    public void run() {
        try {
            setThisServerFiles(); //compute MD5 Checksum (Task=size()): Had problem with 3-4GB files when calculating linear
            queueMessage("#> Calculating MD5 checksum for files");
            //===================
            server = new ServerSocket(myPort);
            myFileList = getMyServerFiles();
            queueMessage("#> Server ("+myPort+") "+ serverName +" started. Path: " +folderPath);
            //ACCEPTING HOSTS
            while (isServerRunning()) {
                Host acceptThread = new Host(server.accept(), this);
                setNewClientSocket(acceptThread);
                new Thread(acceptThread).start();
            }

            closeRemoteConnections();
            System.out.println("#> Server is shutting down!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopServer();
        }
    }

    public void stopServer(){
        isServerRunning = false;
        try {
            server.close();
            this.join();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //-------------------------------------------
    //--- HANDLE CONNECTIONS TO OTHER SERVERS ---
    public Host createNewConnection(String connectTo){
        if (!this.getServerName().equals(connectTo.toUpperCase())) {
            try {
                Host newHost =  new Host(this, connectTo);
                myConnections.add(newHost);

                return newHost;
            } catch (Exception e) {
                queueMessage(e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            queueMessage("--- Smart... connecting to Yourself (Are you 5?) ---");
            System.out.println("Kidding me?");
        }
        return null;
    }

    public Host getConnectionByName(String whichConnection){
        for (Host check : myConnections) {
            if(check.getConnectedTo().equals(whichConnection)) {
                return check;
            }
        }
        return null;
    }

    void removeDisconnected(Host hostName){
        if (hostName != null && myConnections.contains(hostName))
            myConnections.remove(hostName);
        //NOTIFY LISTENERS
        listeners.forEach(l -> l.onReadingChange());
    }

    public synchronized void closeRemoteConnections(){
        myConnections.stream().forEach(
                c -> {
                    try {
                        c.getOut().writeUTF("logout");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
    //-------------------------------------------
    //--- HANDLE CONNECTIONS TO SERVER ---
    private void setNewClientSocket(Host newClient){
        checkClientsConnections();
        clientHandlerList.add(newClient);
        count = clientHandlerList.size();
    }

    public synchronized void checkClientsConnections(){
        clientHandlerList.removeIf( c ->
            c.getClientSocket().isClosed() );
    }

    public synchronized int getCounter() {
        return count;
    }
    //-------------------------------------------
    //--- HANDLE FiLE LIST & MD5 ---
    public void setThisServerFiles(){
        initMyFileListAndGetSize();
        //----------------------
        Runnable calculating = () -> {
            Map<String, String> concurrentHashMap = new ConcurrentHashMap<>(myFileList);
            concurrentHashMap.keySet().stream().parallel().forEach(key -> {
                try {
                    File tmp = Paths.get(getFolderPath() + "/" + key).toFile();
                    String check = getMD5(tmp);
                    myFileList.put(String.valueOf(key), check);
                } catch (Exception e) {
                    e.printStackTrace();
                    queueMessage("#> MD5 Checksum: "+ e.getMessage());
                }
            });
            queueMessage("#> MD5 Checksum calculations done");
        };
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(calculating);
        executorService.shutdown();
    }
    protected String getMD5(File file) {
        MD5Checksum task = new MD5Checksum(file);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(task);
        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            executorService.shutdown();
        }
        return "Null";
    }

    private void initMyFileListAndGetSize(){
        if (Files.exists(Paths.get(folderPath))) {
            Path aDirectory = Paths.get(folderPath);

            try (Stream<Path> paths = Files.walk(aDirectory)) {
                    paths.filter(Files::isRegularFile).parallel().forEach(f -> {
                                myFileList.put(f.getFileName().toString(), "Null");
                                fileNamesList.add(f.getFileName().toString());
                            });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (myFileList.isEmpty())
            myFileList.put("No files to download.", "Null");
    }

    private List<String> getFilePaths(){
        List<String> tmpNames = new ArrayList<>();

        if (Files.exists(Paths.get(folderPath))) {
            Path aDirectory = Paths.get(folderPath);

            try (Stream<Path> paths = Files.walk(aDirectory)) {
                paths.filter(Files::isRegularFile).parallel()
                        .forEach(f -> tmpNames.add(f.getFileName().toString()) );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (myFileList.isEmpty())
            myFileList.put("No files to download.", "Null");

        return new ArrayList<>(tmpNames);
    }

    private void checkUserManualDeletion(){
        List<String> toRemove = new ArrayList<>();
        List<String> tmpNames = getFilePaths();
        Map<String, String> concurrentHashMap = new ConcurrentHashMap<>(myFileList);

        if (tmpNames.size() < myFileList.size()){
            concurrentHashMap.keySet().stream().parallel().forEach(name -> {
                    if (!tmpNames.contains(name))
                        toRemove.add(name);
                });

            toRemove.stream().forEach(key -> {
                myFileList.remove(key);
                fileNamesList.remove(key);
            });

            clientHandlerList.forEach(c -> c.sendCommand(9,this.serverName));
        }
    }

    protected void updateFileList(String newFileName){
        if (Files.exists(Paths.get(folderPath+"/"+newFileName))) {
            try {
                File tmp = Paths.get(folderPath+"/"+newFileName).toFile();
                String check = getMD5(tmp);
                myFileList.put(newFileName, check);
                if (!fileNamesList.contains(newFileName)) {
                    fileNamesList.add(newFileName);
                    queueMessage("#> MD5 of new file: ["+ check +"]");

                    clientHandlerList.forEach(c -> c.sendCommand(8, this.serverName));
                }
            } catch (Exception e) {
                e.printStackTrace();
                queueMessage(e.getMessage());
            }
        }
    }

    public Map<String, String> getMyServerFiles() {
        checkUserManualDeletion();
        return new HashMap<>(myFileList);
    }

    //--- Run after all splits are finished! ---
    private void joinDownloadedFiles(String fileName, boolean resume){
        try {
            Utility util = new Utility();
            util.joinFiles(folderPath+"/"+fileName, getServerMaxSplits(), folderPath, resume);//HERE HAVE TO ASK SERVER
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    //-------------------------------------------
    //--- JOIN DOWNLOADED SPLITS TO ONE FILE ---
    synchronized void setFinishedPart(String fileName, boolean resume){
        finishedParts++;
        if (finishedParts == getServerMaxSplits() ){
            joinDownloadedFiles(fileName, resume);
            finishedParts = 0;
            queueMessage("--- Downloaded successfully ---\n"+ fileName );
            updateFileList(fileName);
            clientHandlerList.forEach(c -> c.sendCommand(8,this.serverName));
        }

        if (finishedParts > getServerMaxSplits()) {
            queueMessage("--- To many downloaded parts of "+fileName+"! ---");
            finishedParts = 0;
        }
    }

    void addStoppedTransfer(FileTransfer newTransfer) {
        String hostName = newTransfer.getHostName();
        FileTransfer exist = getStoppedFileTransfer(hostName);
        List<FileTransfer> list = Collections.synchronizedList(stoppedTransfers);

        synchronized(list) {
            if (exist == null){
                list.add(newTransfer);
                setServerMaxSplits(newTransfer.getTotalSplits());
            }
            else {
                removeFinishedTransfer(exist);
                list.add(newTransfer);
            }
            System.out.println("RESUME size: " +stoppedTransfers.size());
        }
    }

    void removeFinishedTransfer(FileTransfer finished){
        if (finished != null) {
            List<FileTransfer> list = Collections.synchronizedList(stoppedTransfers);

            synchronized(list) {
                if (!list.isEmpty())
                    list.remove(finished);
            }
        }
    }

    FileTransfer getStoppedFileTransfer(String hostName){
        //DEBUGGING
        System.out.println("RESUME size: " +stoppedTransfers.size());
        stoppedTransfers.stream().parallel().
                filter(f -> f.getHostName().equals(hostName)).
                    forEach(x -> System.out.println(x.getHostName()+"|"+x.getMySplit()+"|"+x.getWhereStopped()+"|"+x.getLength()));
        System.out.println("----------------------------------------------------------------------------------");
        //=====================
        return stoppedTransfers.stream().parallel().
                filter(f -> f.getHostName().equals(hostName))
                .findFirst()
                .orElse(null);
    }

    //SETTERS GETTERS
    void queueMessage(String msg) {
        try {
            messagesQueue.add(msg);
            fireMessageChangeEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConcurrentLinkedQueue<String> getMessagesQueue() {
        return messagesQueue;
    }

    private void fireMessageChangeEvent() {
        logListeners.forEach( logListener -> logListener.onIncomingMessage());
    }

    public synchronized void setServerMaxSplits(int serverMaxSplits) {
        this.serverMaxSplits = serverMaxSplits;
    }

    public int getServerMaxSplits(){
        return this.serverMaxSplits;
    }

    public synchronized void setPushFile(String pushFile) {
        this.pushFile = this.getFolderPath() +"/"+pushFile;
    }

    public String getPushFile() {
        return pushFile;
    }
/*
    public Map<String, String> getMyFiles() {
        ///=================
        return "??????";
    }
*/
    public List<String> getFileNamesList() {
        checkUserManualDeletion();
        return fileNamesList;
    }

    public String getFolderPath(){
        return this.folderPath;
    }

    public boolean isServerRunning() {
        return isServerRunning;
    }

    public String getServerName() {
        return serverName.toUpperCase();
    }

    List<String> getConnectNames() {
        List<String> tmpNames = new LinkedList<>();
        myConnections.forEach(e-> tmpNames.add(e.getConnectedTo()) );

        return tmpNames;
    }

}
