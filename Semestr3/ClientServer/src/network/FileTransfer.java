package network;

public class FileTransfer implements Transfer {

    private final String hostName;
    private final String fileName;
    private final long onRead;
    private final long length;
    private final int splitNumber;
    private final int totalSplits;

    public FileTransfer(String hostName, String fileName, long onRead, long length, int splitNumber, int totalSplits) {
        this.hostName = hostName;
        this.fileName = fileName;
        this.onRead = onRead;
        this.length = length;
        this.splitNumber = splitNumber;
        this.totalSplits = totalSplits;
    }

    //--- GETTERS ---
    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public long getWhereStopped() {
        return onRead;
    }

    @Override
    public long getLength() {
        return length;
    }

    @Override
    public int getMySplit() {
        return splitNumber;
    }

    @Override
    public int getTotalSplits() {
        return totalSplits;
    }
}
