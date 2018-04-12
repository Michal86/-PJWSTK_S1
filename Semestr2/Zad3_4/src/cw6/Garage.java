package cw6;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class Garage<T> {

    private List<T> garageObjects;

    public Garage(){
        this.garageObjects = new ArrayList<T>();
    }

    //--- store things inside ---
    public void addGarageObj(T newThing){
        garageObjects.add(newThing);
    }

    //--- put out all things ---

    public void clearGarage(){
        //garageObjects.removeIf( g-> garageObjects.contains(garageObjects.get(garageObjects.size()-1)) ); //still removes from the 1st
        //--------------

        for (int i=garageObjects.size()-1; i>=0; i--) {
            System.out.println("### Item to remove ###\n" + garageObjects.get(i));
            garageObjects.remove( i);
            System.out.println("[Items left " + i+"]");
        }
        //--------------
/*        while (garageObjects.size()>0) {
            System.out.println("### Item to remove ###\n" +
                    garageObjects.stream().
                            filter(g -> g.equals(garageObjects.get(garageObjects.size() - 1)))
                            .findFirst().
                            map(g -> {
                                garageObjects.remove(g);
                                return g;
                            }
                    )
            );
        }*/
    }

    //--- check out ---
    public void listGarageObjects(){
        if (garageObjects.size()>0) {
            garageObjects.forEach(item -> System.out.println(
                    "Item no " + (garageObjects.indexOf(item) + 1) + "\n" +
                            "(Object type : " + item.getClass().getSimpleName() + ")\n" + item)
            );
        }
        else
            System.out.println("Garage is empty!");
    }
}
