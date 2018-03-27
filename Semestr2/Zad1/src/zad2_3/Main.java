package zad2_3;

public class Main {

    public static void main(String[] args){
        ZbrojaFactory noweZbroje = new ZbrojaFactory();
        stworzZbroje(noweZbroje);
        //Sortuj wg maksPredkosc, gdy rowne wtedy po sile Repulsorow
        noweZbroje.sortujZbroje();

        System.out.println("Testowanie");
        ZbrojaTesty testy = new ZbrojaTesty(noweZbroje.getZbroje());
        testy.testPredkosci();
        testy.testRepulsory();
    }

    //---
    static void stworzZbroje(ZbrojaFactory noweZbroje){
        /* stwórz anonimową klasę wewnętrzną Mark1 */
        ZbrojaBuilder mark1 = new ZbrojaBuilder(){
            @Override
            public void latanie(int predkosc) {
                try {
                    throw new BrakFunkcjonalnosciException();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void repulsory(int silaRepulsorow) {
                try {
                    throw new BrakFunkcjonalnosciException();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        noweZbroje.dodajZbroje(mark1);
        //--- Modele od Mark 2 ---
        ZbrojaBuilder mark2a = new ZbrojaBuilder.Builder("Mark II v1.0")
                .predkosc()
                .silaRepulsorow(60)
                .iloscFlar(10)
                .maksymalnaPredkosc(152)
                .create();
        noweZbroje.dodajZbroje(mark2a);

        ZbrojaBuilder mark2b = new ZbrojaBuilder.Builder("Mark II v1.1")
                .predkosc()
                .silaRepulsorow(70)
                .iloscFlar(10)
                .maksymalnaPredkosc(152)
                .create();
        noweZbroje.dodajZbroje(mark2b);

        ZbrojaBuilder mark3 = new ZbrojaBuilder.Builder("Mark III v1.0")
                .predkosc()
                .silaRepulsorow(90)
                .iloscFlar(14)
                .maksymalnaPredkosc(192)
                .create();
        noweZbroje.dodajZbroje(mark3);

        ZbrojaBuilder mark4 = new ZbrojaBuilder.Builder("Mark IV v1.0")
                .predkosc()
                .silaRepulsorow(110)
                .iloscFlar(24)
                .maksymalnaPredkosc(252)
                .create();
        noweZbroje.dodajZbroje(mark4);
    }
}
