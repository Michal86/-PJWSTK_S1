package zadania_4_6;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexChecker {
    private Pattern checkReg;
    private Matcher regexMatcher;
    private ArrayList<Wiadomosc> wiadomosci;
    private int counter;

    public RegexChecker(String theRegex, StringBuilder strBld2Check) throws Exception{
        int counter = 0;
        wiadomosci = new ArrayList<Wiadomosc>();
        checkReg   = Pattern.compile(theRegex);
        regexMatcher = checkReg.matcher(strBld2Check);
    }//^regexChecker

    //=== METODY ===
    public void collectWiadomosc() {
        Wiadomosc newWiadomosc = null;
        boolean sameLog = false;

        while ( regexMatcher.find() ) {
            if (regexMatcher.group().length() != 0) {
                //--- if we come across "Logged on"
                if( regexMatcher.group(18)!=null) {
                    newWiadomosc = new Wiadomosc();
                    if (regexMatcher.group(10) != null) {
                        newWiadomosc.setUser(regexMatcher.group(10));    //user name
                    }
                }
                if( newWiadomosc != null && !newWiadomosc.getUser().isEmpty()){
                    if (!sameLog) {
                        //--- PREPARE WIADOMOSC 1st input---
                        newWiadomosc.setData(Integer.parseInt(regexMatcher.group(2)),
                                Integer.parseInt(regexMatcher.group(3)),
                                Integer.parseInt(regexMatcher.group(4)));
                        newWiadomosc.setGodzina(regexMatcher.group(5));
                        newWiadomosc.setIp(regexMatcher.group(11));
                        newWiadomosc.setTekst(regexMatcher.group(17));
                        //--- ADD WIADOMOSC TO ARRAYLIST---
                        addData(newWiadomosc);
                        sameLog = true;
                    }
                    else {
                        //--- ADD new log text w/o making new instance of WIADOMOSC ---
                        //--- Text to godzine + tresc
                        String addToTxt = newWiadomosc.getTekst();
                        addToTxt += "\n" + regexMatcher.group(5) +" > "+ regexMatcher.group(17);
                        //System.out.println(addToTxt);
                        newWiadomosc.setTekst(addToTxt);
                    }
                    if ( regexMatcher.group(17).contains("disconnected")) {
                        sameLog = false;
                        newWiadomosc = null;
                    }
                }
                counter++;
            }
        }
        System.out.println("STATYSTYKI:");
        System.out.println(String.format("Counter regex: %d", counter));
        System.out.println(String.format("Groups captured: %d", regexMatcher.groupCount()));
    }

    // Add a new Wiadomosc to ArrayList
    public Wiadomosc addData(Wiadomosc wiadomoscToAdd){
            wiadomosci.add(wiadomoscToAdd);
            return  wiadomoscToAdd;
    }

    // Displays all from wiadomosci [ArrayList<Wiadomosc>]
    public void displayAllFindings(){
        System.out.println("=== Zebrane wiadomosci ===");
        for(Wiadomosc msg : wiadomosci)
            System.out.println(msg);

    }

    // Displays wiadomosc only from specified day
    public void displayWiadomosc(int miesiac, int dzien, int rok){
        boolean found = false;
        try{
            Wiadomosc getMsgDnia = new Wiadomosc(miesiac, dzien, rok);
            if (getMsgDnia != null) {
                displayInfo(miesiac, dzien);
                for (Wiadomosc msg : wiadomosci) {
                    if (msg.equalsData(getMsgDnia)) {
                        System.out.println(msg);
                        found = true;
                    }
                    //szukaj po dniu
                    else if( getMsgDnia.getDzien() == msg.getDzien() && miesiac==-1){
                        System.out.println(msg);
                        found = true;
                    }
                    //szukaj po miesiacu
                    else if( getMsgDnia.getMiesiac() == msg.getMiesiac() && dzien==-1){
                        System.out.println(msg);
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("Brak danych.");
                }
            } else
                System.out.println("Could NOT make search!");
        }
        catch (Exception e){
            System.err.println("Something is wrong inside [displayWiadomosc].");
        }
    }

    //---
    public void displayInfo(int miesiac, int dzien){
        if (dzien == -1)
            System.out.println("=== Wiadomosci z miesiaca [" + miesiac + "] ===");
        if (miesiac == -1)
            System.out.println("=== Wiadomosci z dnia [" + dzien + "] ===");
    }
    public int getCounter(){
        return counter;
    }
    // Displays wiadomosc only from specified day
    public void displayWiadomoscIp(String ip){
        boolean found = false;

        for (Wiadomosc msg : wiadomosci) {
            if (msg.getIp().equals(ip)) {
                System.out.println(msg);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Brak danych z zadanym ip.");
        }
    }

    // Check if log in data is from the same day
    public boolean checkWiadomosc(Wiadomosc toCheck){
        boolean exists = false;

        for(Wiadomosc current : wiadomosci) {
            if ( current.equalsData(toCheck) )
                exists = true;
        }

        return exists;
    }
}
