/* Michal Radzewicz
 * ---- MALA ZMIANA ---
 * Na wstepie zalozylem, ze zbieram tylko potrzebne dane, czyli te po zalogowaniu
 * Jezeli chce zbierac wszystko to w metodzie collectWiadomosc() dodam if( regexMatcher.group(18) = null)
 * i wewnatrz stworze instancje klasy wiadomosc w ktorej zbiore dane i dodam do ArrayList<Wiadomosc>
 *
 * ---- ZADANIA ----
 * V. Przygotuj klase Wiadomosc
 * Zaimplementuj konstruktor i przeciaz metode toString.
 * ----
 * VI. Wczytaj zawartosc pliku serverLog.txt. Podziel odczytane dane tak, aby kazda linia
 * inicjowała obiekt klasy Wiadomosc, a nastepnie umiesc obiekty w kolekcji ArrayList.
 * Nastepnie utwórz metody które pozwola na:
 * wyswietlenie wszystkich wiadomosci z zadanego dnia
 * wyswietlenie wszystkich wiadomosci z zadanego miesiaca
 * wyswietlenie wszystkich wiadomosci z zadanego adresu ip
 * ----
 * Dodalem wyswietlenie wszystkich wiadomosci z podanej calej daty
 */

package zadania_4_6;
import java.io.IOException;


public class Main_Zad3_4 {
    public static void main(String[] args){
        MakeStringBuilder strBuilder;
        RegexChecker regToFind;
        UserInterface ui;
        //===============================================================
        // "mm/dd/yyy" ->//4 grupy [4]
        final String DATE_PATTERN = "((0?[1-9]|1[012])/(3[0-1]|[0-2]\\d{1})/(\\d{4}))";
        // "h:mm:ss" AM >//4 grupy [4+4](pierwsza zawiera palna godzine z napisem AM)
        final String HOUR_PATTERN = "((\\d{1,2}|1[0-2]{1,2}):([0-5]\\d{1}):([0-5]\\d{1}) AM)";
        // USER "user name" ->//2 grupy [8+2] (w grp 10 jest nazwa)//pobieram, gdy jest zalogowany [CZY DOBRE ZALOZENIE?]
        final String USER_PATTERN = "( - ([\\w]+) )?\\(";
        // (“1.1.1.1  to 255.255.255.255” ->//5 grup [10+5] (11-ta zawiera pelne IP)
        final String IP_ADDRESS_PATTERN = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.+" +
                                           "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.+" +
                                           "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.+" +
                                           "([01]?\\d\\d?|2[0-4]\\d|25[0-5]))";

        // MSG ->//3 grupy [15+3] (start collecting -> if "Logged on" found [grp 18]))
        final String MSG_PATTERN = "(> )((.*Logged on)?.*)";
         //======================================================================================
        String theRegex = DATE_PATTERN+ ".*" +HOUR_PATTERN + USER_PATTERN + IP_ADDRESS_PATTERN+ ".*" + MSG_PATTERN;
        //======================================================================================
        try {
            //build strBuilder (StringBuilder)
            strBuilder = new MakeStringBuilder("serverLog.txt");
            //creates instance od class RegexChecker input: theRegex + strBuilder
            regToFind = new RegexChecker(theRegex, strBuilder.getStringBuilder());
            // Use regex to create wiadomosci IF LOGGED ON (dont collect other data)
            regToFind.collectWiadomosc();
            // create instance of class: display menu + handle input from the user
            ui = new UserInterface(regToFind);
            ui.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
