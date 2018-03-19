import com.sun.org.apache.xpath.internal.axes.MatchPatternIterator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class FirstRegex {
    public static void main(String[] args) {
        String text = "aaaabbcchaa\n" +
                      "bbaaaacch\n" +
                      "ccaaacch\n" +
                      "bbaaaabbh\n" +
                      "abch\n";

        String reg1 = "[abch]+";    //<- wyrazenie
        //======================================
        try{
            //---- ZAD 1 ----
            Pattern p = Pattern.compile(reg1);
            Matcher m = p.matcher(text);
            //System.out.println("Text: \n" + text);
            System.out.println("Zad1. -> Wyrazenie regularne akceptujace wszystkie ciągi.");
            while( m.find() ){
                if ( m.group().length() != 0) {
                    System.out.println(m.group());
                }
            }
            m.reset();
            System.out.println("----------");

            //---- ZAD 2 ----
            text = "Wieś w Polsce połozona w województwie wielkopolskim, w powiecie" +
                    "kolskim, w gminie Olszówka. \nW latach 1975-1998 miejscowosc połozona była w województwie" +
                    "koninskim.";
            String reg2 = "\\d{2}";    //<- wyrazenie
            //======================================
            p = Pattern.compile(reg2);
            m = p.matcher(text);
            int ilePar = 0;
            System.out.println("Zad2. -> Ile par liczb, zawiera tekst.");
            System.out.println(text);
            while( m.find() ){
                if ( m.group().length() != 0) {
                    ilePar++;
                    System.out.print("["+m.group()+"]");
                }
            }
            System.out.println("\nZnaleziono par: " +ilePar);
            System.out.println("----------");
            m.reset();

        }
        catch (IndexOutOfBoundsException s){
            System.err.println("There is no capturing group in the pattern with the given index.");
            //s.printStackTrace();
        }
        catch(Exception e){
            System.err.println("Error occured.");
        }
    }//end of main





}
