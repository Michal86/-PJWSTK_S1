/*
@Michał Radzewicz
@Source 1.txt: https://en.wikipedia.org/wiki/Game_programming

Zad 3
Uzywajac wyrazen regularnych policz
ile razy wystapiły elementy bedace słowami.

-> KTORE WYRAZENIE JEST LEPIEJ WIDZIANE? :
      "[a-zA-Z]{2,}" VS "\\p{L}{2,}"
*/

import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ThirdRegex {

    public static void main(String[] args){
        int wordsCounter = 0;
        StringBuilder sbFromFile;
        String reg3 = "\\p{L}{2,}";   //moje wyrazenie do znalezienia wyrazow
        //String reg3 = "[a-zA-Z]{2,}";   //czy, tak tez moze byc?
        //----
        try{
            //-> czyta plik i tworzy StringBuilder
            sbFromFile = getString("1.txt");
            //-> podlicza wystapienia w sbFromFile wg podanego wyrazenia
            wordsCounter = regexChecker(reg3, sbFromFile);
            //-> wyswietl wynik
            displayResult(wordsCounter);
        }
        catch(IOException e){
            System.err.println("An I/O Error Occurred.");
            System.exit(0);
        }
        catch (IndexOutOfBoundsException s){
            System.err.println("There is no capturing group in the pattern with the given index.");
            //s.printStackTrace();
        }
        catch (Exception ex){
           ex.printStackTrace();
           System.exit(0);
        }
    }//^MAIN

    //======================================
    //Find regex and return counter[int]-> inputs: sb [StringBuilder] and theRegex [Pattern.compile()]
    public static int regexChecker(String theRegex, StringBuilder strBld2check) throws Exception{
        int counter = 0;
        Pattern checkReg   = Pattern.compile(theRegex);
        Matcher regexMatcher = checkReg.matcher(strBld2check);

        while( regexMatcher.find() ){
            if( regexMatcher.group().length() != 0 ){
                //System.out.print("["+regexMatcher.group()+"]"); //<- do wyswietlenia znalezionych wyrazow
                counter++;
            }
        }
        //System.out.println();
        return counter;
    }//^regexChecker

    //"Loads" file and creates my string, I'll use it for Regex.
    public static StringBuilder getString(String fName) throws Exception{
        StringBuilder sb = new StringBuilder();
        FileInputStream fis = new FileInputStream(fName);
        int content = 0;

        while( (content=fis.read()) != -1 ){
            sb.append( (char)content);
        }

        fis.close();
        return sb;
    }//^getString

    //display results
    public static void displayResult(int counter){
        System.out.println("==== [ How many words in the file ] ====");
        System.out.println(" Words counter = "+ counter );
        System.out.println("========================================");
    }
}
