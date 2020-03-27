package zad1;

import static java.nio.file.FileVisitResult.*;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.EnumSet;

public class MySimpleFileVisitor extends SimpleFileVisitor<Path> {

  private FileChannel outFileChannel;
  private ByteBuffer byteBuffer;

  Charset CpSet = Charset.forName("Cp1250"),
          toUTF = Charset.forName("UTF-8");

  public MySimpleFileVisitor(Path outFilePath) throws IOException {
    this.outFileChannel = FileChannel.open(outFilePath, EnumSet.of(CREATE, APPEND));
  }

  private void recodeForUtf(FileChannel fileChannel, long bufferSize){
    byteBuffer = ByteBuffer.allocate((int)bufferSize +1);
    byteBuffer.clear();

    try {

      fileChannel.read(byteBuffer);
      byteBuffer.flip();
      CharBuffer cbuf = CpSet.decode(byteBuffer);
      ByteBuffer buffer = toUTF.encode(cbuf);

      while( buffer.hasRemaining() ){
        this.outFileChannel.write(buffer);
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public FileVisitResult visitFile(Path file_path, BasicFileAttributes attr) {
    System.out.format("File name: %s ", file_path);
    System.out.println("[(]" + attr.size() + "bytes]");
    try{
      this.recodeForUtf(FileChannel.open(file_path), attr.size());
    }catch(IOException ex){
      System.out.format("Błąd przy otwieraniu pliku: %s ", file_path);
    }
    return CONTINUE;
  }

  @Override
  public FileVisitResult visitFileFailed(Path file_path, IOException exc) {
    System.err.println("VisitFileFailed: " + exc);
    return CONTINUE;
  }
}
