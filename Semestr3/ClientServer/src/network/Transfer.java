package network;

import java.io.Serializable;

public interface Transfer extends Serializable
{
    String getHostName();
    String getFileName();
    long getWhereStopped();
    long getLength();
    int getMySplit();
    int getTotalSplits();
}

