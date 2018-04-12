package cw3;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Stwórz klasę public class Czlowiek{ ta klasa posiada atrybuty imię, wiek, kolorOczu i kolorWlosow,
 * dwa ostatnie atrybuty mają być typami wyliczeniowymi.
 * Nadpisz metodę toString by wypisywała wszystkie wartości. Stwórz obiekt tej klasy i wypisz jej wartości.
 *
 */
public class Human {

    private String name;
    private int age;
    private Color eyes;
    private Color hair;

    private enum Color{
        WHITE, GREEN, BLUE, BLONDE,  BROWN, BLACK
    }


    public Human(String name, int age, Color eyes, Color hair){
        this.name = name;
        this.age = age;
        this.eyes = eyes;
        this.hair = hair;
    }

    //--- getters & setters ---
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Color getEyes() {
        return eyes;
    }

    public Color getHair() {
        return hair;
    }

    @Override
    public String toString() {
        return "Name: "+name+", age: "+age+", eye color: "+eyes+", hair color: "+hair+"\n";
    }

    //==============================================================
    public static void main(String[] args) {
        List<Human> listOfPeople = Arrays.asList(
                new Human("Michal", 31, Color.BROWN, Color.BROWN),
                new Human("Batman", 40, Color.BLACK, Color.BLACK),
                new Human("Thor", 40, Color.BLUE, Color.BLONDE),
                new Human("Star-Lord", 37, Color.BLUE, Color.BLONDE)
        );

        System.out.println("---- Method reference ----");
        listOfPeople.forEach(System.out::println);
        System.out.println("---- Lambda ----");
        listOfPeople.forEach( human-> System.out.println(human));

        System.out.println("Stream and filter(\"M\")");
        listOfPeople.stream()
                .filter(h->h.getName().contains("M"))
                .forEach(System.out::println);

        //---  testing another stream ---
        System.out.println("Use stream.filter() to filter a List, and .findAny().orElse (null) to return an object");
        Human result = listOfPeople.stream()                                    // Convert to steam
                .filter(h -> Color.BLUE==h.getEyes() && 37==h.getAge())         // person with blue eyes and age 37
                .findAny()                                                      // If 'findAny' then return found
                .orElse(null);                                            // If not found, return null
        System.out.println(result);
    }
}
