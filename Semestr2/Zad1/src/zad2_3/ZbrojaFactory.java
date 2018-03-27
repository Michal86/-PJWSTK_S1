package zad2_3;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ZbrojaFactory {

    private List<ZbrojaBuilder> zbroje;

    public ZbrojaFactory(){
        this.zbroje = new LinkedList<>();
    }

    public void dodajZbroje(ZbrojaBuilder nowaZbroja){
        this.zbroje.add(nowaZbroja);
    }

    public void sortujZbroje(){
        Collections.sort(zbroje);
        pokazZbroje("=== Wyswietlam posortowane Zbroje ===");
    }

    public void pokazZbroje(String str){
        System.out.println(str);
        for(ZbrojaBuilder current : zbroje){
            System.out.println(current);
        }
    }

    public List<ZbrojaBuilder> getZbroje(){
        return this.zbroje;
    }
}
