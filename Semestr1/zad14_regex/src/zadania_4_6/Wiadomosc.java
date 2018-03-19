package zadania_4_6;

public class Wiadomosc {
    private int dzien,miesiac, rok;
    private String godzina;
    private String user;
    private String ip;
    private String tekst;

    public Wiadomosc(){}

    public Wiadomosc(int miesiac, int dzien ,int rok){
        this.miesiac = miesiac;
        this.dzien = dzien;
        this.rok = rok;
    }

    //--- GETTERS ---

    public String getUser() {
        return user;
    }

    public int getDzien(){
        return this.dzien;
    }

    public int getMiesiac() {
        return miesiac;
    }

    public int getRok() {
        return rok;
    }

    public String getIp() {
        return ip;
    }

    public String getTekst() {
        return tekst;
    }

    //--- SETTERS ---
    public void setData(int miesiac, int dzien, int rok) {
        this.dzien = dzien;
        this.miesiac = miesiac;
        this.rok = rok;
    }

    public void setGodzina(String godzina) {
        this.godzina = godzina;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }
    //---
    public boolean equalsData(Wiadomosc obj) {
        if (obj == null) return false;
        int objDzien = obj.getDzien();
        int objMies  = obj.getMiesiac();
        int objRok   = obj.getRok();
        //---------------------
        if ( this.rok == objRok && this.miesiac == objMies && this.dzien == objDzien) {
            return true;
        }
        else
            return false;
    }

    @Override
    public String toString() {
        return "Wiadomosc{" +
                "Dzien=" + dzien +
                ", Miesiac=" + miesiac +
                ", Rok=" + rok +
                ", Godzina='" + godzina + '\'' +
                ", User='" + user + '\'' +
                ", IP='" + ip + '\'' +
                ", Tekst:'\n" + tekst + "'}\n" +
                "---------------------------";
    }
}
