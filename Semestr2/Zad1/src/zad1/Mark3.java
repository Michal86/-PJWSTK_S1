package zad1;

public class Mark3 extends Mark2{

    public Mark3(int predkosc, int sila, int ilosc) {
        super(predkosc, sila, ilosc);
    }

    public void wystrzelPociski(){
        ilośćPocisków -= 1;
    }

    @Override
    public void latanie(int predkosc) {
        super.latanie(predkosc);
    }

    @Override
    public void repulsory(int sila) {
        super.repulsory(sila);
    }

    @Override
    public void flary(int ilość) {
        super.flary(ilość);
    }
}
