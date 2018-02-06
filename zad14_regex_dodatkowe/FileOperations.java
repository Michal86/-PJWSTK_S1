package zadania.regex.dodatkowe;

import java.io.*;

public class FileOperations {
    private static File file;
    private static StringBuilder strBld;

    public static void handleFile(String fileName){
        file = new File(fileName);

        try( BufferedReader inputStream = new BufferedReader(
                new InputStreamReader( new FileInputStream(file),"UTF8")) ) {
            int content;
            strBld = new StringBuilder();

            while((content=inputStream.read()) != -1 ) {
                strBld.append((char) content);
            }
            /*//Another method using string
            String str;

            while ((str = inputStream.readLine()) != null) {
                System.out.println(str);
            }*/
           //System.out.println(strBld);
        }
        catch (UnsupportedEncodingException e){
            System.out.println(e.getMessage());
        }
        catch (FileNotFoundException e){
            System.out.println("File not found.");
            //e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }//END OF createFis

    public static StringBuilder getStrBld() {
        return strBld;
    }

    public static void display(){
        System.out.println("***********************************");
        System.out.println(strBld.toString());
        System.out.println("***********************************");

    }

}
