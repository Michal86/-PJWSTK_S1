package zad2_3;
/**
 * User: Michał Radzewicz
 * Date: 2018-03-27
 * Trying to code it with FLUENT INTERFACE design pattern
 */
public class ZbrojaBuilder extends Zbroja implements Comparable<ZbrojaBuilder> {

    private String model;
    private int predkosc;
    private int silaRepulsorow;
    private int iloscFlar;
    private int maksymalnaPredkosc;
    //-------------------------------

    protected ZbrojaBuilder(Builder builder){
        this.model = builder.model;
        this.predkosc = builder.predkosc;
        this.silaRepulsorow = builder.silaRepulsorow;
        this.iloscFlar = builder.iloscFlar;
        this.maksymalnaPredkosc = builder.maksymalnaPredkosc;
    }

    public ZbrojaBuilder() {
        this.model = "Mark I";
        this.predkosc =0;
        this.silaRepulsorow = 0;
        this.iloscFlar = 9;
        this.maksymalnaPredkosc = 0;
    }

    //Sortuj od najszybszego do najwolniejszego modelu, gdy rowne po sile repulsorow
    @Override
    public int compareTo(ZbrojaBuilder zbrojaToCompare) {
        int result = zbrojaToCompare. maksymalnaPredkosc() - this.maksymalnaPredkosc;
        if (result == 0)
            return (zbrojaToCompare.silaRepulsorow() - this.silaRepulsorow);
        else
            return result;
    }

    //--- getters ---
    public String getModel() {
        return model;
    }

    //--- settery ---
    @Override
    public void latanie(int predkosc) {
        this.predkosc += predkosc;
        System.out.println(model+"->"+"aktualna predkosc: "+ this.predkosc);
    }

    @Override
    public void repulsory(int silaRepulsorow) {
        this.silaRepulsorow = silaRepulsorow;
        System.out.println(model+"->"+"aktualna sila repulsorow: " + this.silaRepulsorow);
    }

    @Override
    public void flary(int iloscFlar) {
        this.iloscFlar = iloscFlar;
        System.out.println("Dodaj flary: "+ iloscFlar);
    }

    @Override
    public int maksymalnaPredkosc() {
        return this.maksymalnaPredkosc;
    }

    @Override
    public int silaRepulsorow() {
        return this.silaRepulsorow;
    }

    @Override
    public String toString() {
        return "[Model: "+model+", predkosc: "+predkosc+", silaRepulsorow: "+silaRepulsorow+
                ", iloscFlar: "+iloscFlar+", maksymalnaPredkosc: "+ maksymalnaPredkosc +"]\n";
    }

    //--- Zbroja Builder ---
    public static class Builder{
        private String model;
        private int predkosc;
        private int silaRepulsorow;
        private int iloscFlar;
        private int maksymalnaPredkosc;

        public Builder(String model){
            this.model = model;
        }

        public Builder predkosc(){
            this.predkosc = 0;
            return this;
        }

        public Builder silaRepulsorow(int silaRepulsorow){
            this.silaRepulsorow = silaRepulsorow;
            return this;
        }

        public Builder iloscFlar(int iloscFlar){
            this.iloscFlar = iloscFlar;
            return this;
        }

        public Builder maksymalnaPredkosc(int maksymalnaPredkosc){
            this.maksymalnaPredkosc = maksymalnaPredkosc;
            return this;
        }

        public ZbrojaBuilder create(){
            return new ZbrojaBuilder(this);
        }
    }
}
