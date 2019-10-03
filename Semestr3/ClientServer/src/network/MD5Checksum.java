package network;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.concurrent.Callable;

public class MD5Checksum implements Callable<String> {
    private File file;

    public MD5Checksum(File file){
        this.file = file;
    }

    public static byte[] createChecksum(File file) throws Exception {
        InputStream fis =  Files.newInputStream(Paths.get(file.getPath()));
        byte[] buffer = new byte[8192];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    public static String getMD5Checksum(File file) throws Exception {
        byte[] b = createChecksum(file);
        String result = "";

        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

    @Override
    public String call(){
        try {
            return getMD5Checksum(file);
        }
        catch (InterruptedException e) {
            throw new IllegalStateException("Task interrupted", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Null";
    }
}
