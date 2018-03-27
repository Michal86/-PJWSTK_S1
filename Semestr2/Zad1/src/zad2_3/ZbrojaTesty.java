package zad2_3;

import java.util.List;
import java.util.Random;

public class ZbrojaTesty {

    private List<ZbrojaBuilder> zbrojeDoTestow;
    private Random rand = new Random();


    public ZbrojaTesty(List<ZbrojaBuilder>zbrojeDoTestow){
        this.zbrojeDoTestow = zbrojeDoTestow;
    }

    public void testPredkosci(){
        System.out.println("- Test prędkości -");
        for (ZbrojaBuilder zbroja : zbrojeDoTestow) {
            int predkosc = rand.nextInt(zbroja.maksymalnaPredkosc()+1)+1;
            zbroja.latanie(predkosc);
        }
    }
    //---

    public void testRepulsory(){
        System.out.println("- Test repulsorów -");
        for (ZbrojaBuilder zbroja : zbrojeDoTestow) {
            zbroja.repulsory(zbroja.silaRepulsorow());
        }
    }


}
