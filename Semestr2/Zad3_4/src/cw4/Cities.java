package cw4;

import cw1.UserInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * zad. 4
 * Utwórz liste samych miast. Używając wyrażeń lambda posortuj przez Collections.sort i wypisz listę.
 */
public class Cities {
    List<String> listOfCities;

    public Cities(){
        listOfCities = new ArrayList<>();
    }

    public void addNewCity(String cityName){
        if (!listOfCities.contains(cityName))
            this.listOfCities.add(cityName);
        else
            System.out.println("City already in the list.");
    }

    public List<String> getListOfCities() {
        return listOfCities;
    }

    public void displayCities(){
        listOfCities.forEach( c-> System.out.println("City: " + c));
    }

    //==============================================================

    public static void main(String[] args) {
        Cities myCities = new Cities();
        String answer = "";

        System.out.println("Enter name of the city. [\"-\" ands the input]");
        System.out.print("City name: ");

        while (!(answer=UserInput.getInput()).equals("-") ){
            if (!answer.equals(""))
                myCities.addNewCity(answer);
            System.out.println("--------------------");

            System.out.print("City name: ");
        }

        System.out.println("==== Before sort ====");
        myCities.displayCities();
        //lambda 1
        Collections.sort(myCities.getListOfCities(), (city1, city2)-> city1.compareTo(city2));
        System.out.println("==== After sort ====");
        myCities.displayCities();
        //lambda 2
        Comparator<String> anotherComparator = (c1, c2)->c1.compareTo(c2);
        myCities.getListOfCities().sort(anotherComparator.reversed());
        System.out.println("==== Reversed sort ====");
        myCities.displayCities();
    }

}
