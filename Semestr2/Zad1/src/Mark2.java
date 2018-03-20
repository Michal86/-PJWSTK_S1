import java.util.Random;

public class Mark2 implements Zbroja{

    private String model;
    private int predkosc;
    private int sila;
    private int ilość;


    public Mark2(int predkosc, int sila, int ilosc){
        this.model = "Mark 2";
        latanie(predkosc);
        repulsory(sila);
        flary(ilosc);
    }

    public void ostrzezeniePrzedOblodzeniem(){
        Random rand = new Random();
        int wysokoscLotu = rand.nextInt(800)+1;

        try {
            System.out.println("Wysokość lotu: "+wysokoscLotu+"m");
            if (wysokoscLotu >= 500)
                throw new OblodzenieException("Oblodzenie"); }
        catch (OblodzenieException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void latanie(int predkosc) {
        this.predkosc = predkosc;
    }

    @Override
    public void repulsory(int sila) {
        this.sila = sila;
    }

    @Override
    public void flary(int ilość) {
        this.ilość = ilość;
    }
}
