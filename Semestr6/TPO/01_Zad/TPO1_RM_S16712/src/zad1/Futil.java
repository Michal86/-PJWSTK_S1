package zad1;

import java.nio.file.*;

public class Futil {

  public static void processDir(String dirName, String resultFileName){

    Path outFilePath = Paths.get(resultFileName);
    Path dict = Paths.get(dirName);

    try{
      MySimpleFileVisitor visitor = new MySimpleFileVisitor(outFilePath);
      Files.walkFileTree(dict, visitor);
    }catch(Exception ex){
      System.out.println(ex);
    }

  }

}
