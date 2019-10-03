package network;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class Utility {
    private long partSize;
    private volatile long readAmount;
    private long skipSize;


    //--- sending bytes ---
    public void sendBytes(InputStream from, OutputStream copyTo) {
        byte[] buffer = new byte[8192];
        DataOutputStream dos = new DataOutputStream(copyTo);

        try {
            long fileSize = (long) from.available();
            long onRead = 0L;
            dos.writeLong(fileSize);
            dos.writeLong(0L);   //cause of getBytes

            int read;
            while ((read=from.read(buffer)) != -1 && onRead < fileSize) {
                dos.write(buffer, 0, read);
                onRead += (long) read;
            }
        }
        catch (Exception exe){
            exe.printStackTrace();
        }
    }

    public void getBytes(DataInputStream from, OutputStream copyTo) throws IOException{
        readAmount = 0L;
        byte[] buffer = new byte[8192];
        partSize = from.readLong();
        skipSize = from.readLong(); //CASE: interrupting || DC -> skipSize will be starting point for resuming
        int read;

        while ((readAmount < partSize) && (read = from.read(buffer))>-1) {
            copyTo.write(buffer, 0, read);
            readAmount += (long) read;
        }
    }

    public boolean sendFilePiece(DataOutputStream dos, String fileName, int splitNumber, int splits){
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(fileName, "r");
            long sourceSize = raf.length();
            long bytesPerSplit = sourceSize/ splits;
            long remainingBytes = sourceSize % splits;

            if (splitNumber == 0 || splitNumber>splits) splitNumber = 1;
            long skipSize = bytesPerSplit*(splitNumber-1);
            if(splitNumber == splits) bytesPerSplit +=remainingBytes; //Last HOST sends extra bytes

            informSetStartAndSend(raf, dos, skipSize, bytesPerSplit);

            raf.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                raf.close();
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception n){
            n.printStackTrace();
        }
        return false;
    }

    //--- RESUME DOWNLOADING ---
    public boolean sendFilePiece(DataOutputStream dos, String fileName, long startFrom, long remainingBytes){
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(fileName, "r");
            if (skipSize < remainingBytes)
                remainingBytes -= startFrom;

            informSetStartAndSend(raf, dos, startFrom, remainingBytes);

            raf.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                raf.close();
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception n){
            n.printStackTrace();
        }
        return false;
    }

    private void informSetStartAndSend(RandomAccessFile raf , DataOutputStream dos, long startFrom, long remainingBytes) throws IOException{
        int maxReadBufferSize = 8192;
        dos.writeLong(remainingBytes);  //confirm size
        dos.writeLong(startFrom);       //confirm starting point

        raf.skipBytes((int) startFrom );
        sendParts(raf, dos, remainingBytes, maxReadBufferSize);
    }

    private void sendParts(RandomAccessFile raf , DataOutputStream dos, long bytesPerSplit, int myBuffer) throws IOException{
        if (bytesPerSplit > myBuffer) {
            long numReads = bytesPerSplit / myBuffer;
            long numRemainingRead = bytesPerSplit % myBuffer;

            for (int i = 0; i < numReads; i++) {
                sendSplitBytes(raf, dos, myBuffer);
            }
            if (numRemainingRead > 0)
                sendSplitBytes(raf, dos, numRemainingRead);
        }
        else {
            sendSplitBytes(raf, dos, bytesPerSplit);
        }
    }

    private void sendSplitBytes(RandomAccessFile raf, DataOutputStream dos, long numBytes) throws IOException {
        long onRead = 0L;
        byte[] buffer = new byte[(int) numBytes];

        int read = raf.read(buffer);
        if (onRead<numBytes && read > -1) {
            dos.write(buffer,0, read);
            onRead += (long)read;
            readAmount = onRead;
        }
    }

    //--- Join downloaded files ---
    public void joinFiles(String fileName, int numSplits, String folder, boolean resume) throws IOException{
        boolean ready = true;
        for (int i = 1; i <= numSplits; i++) {
            if (!Files.exists(Paths.get(folder+"/split."+i)))
                ready = false;
        }
        if (ready) {
            if (!resume) Files.deleteIfExists(Paths.get(fileName));

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName, true));
            byte[] buffer = new byte[8192];

            for (int index = 1; index <= numSplits; index++) {
                RandomAccessFile raf = new RandomAccessFile(folder + "/split." + index, "r");
                int count = 0;

                while ((count = raf.read(buffer)) != -1 ) {
                    bos.write(buffer, 0, count);
                }
                raf.close();
            }
            bos.close();
            //remove joined parts
            for (int i = 1; i <= numSplits; i++)
                Files.deleteIfExists(Paths.get(folder + "/split." + i));
        }
    }

    //---
    public synchronized long getPartSize() {
        return partSize;
    }

    public synchronized long getReadAmount() {//we will know where to resume from
        return skipSize+readAmount;
    }
}
