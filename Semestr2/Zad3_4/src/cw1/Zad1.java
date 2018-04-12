package cw1;

import java.util.HashMap;
import java.util.Map;

/**
 * Napisz program który będzie pobierał od użytkownika miasto i państwo aż użytkownik wpisze “-”.
 * Używając kolekcji Map zapisz te państwa i ich miasta. Korzystając z foreach wypisz zawartość
 */
public class Zad1 {
    private Map<String, String> cityList;

    public Zad1(){
        cityList = new HashMap<>();
    }

    public void addNewCity(String cityName, String countryName){
        cityList.put(cityName, countryName);
    }

    public String toString() {
        if (!cityList.isEmpty()) {
            String listToDisplay = "";
            for (String city : cityList.keySet())
                listToDisplay += city + " -> " + cityList.get(city) + "\n";

            return listToDisplay;
        }
        else
            return "No data to display.";
    }

    //==============================================================

    public static void main(String[] args){
        Zad1 cityCountryMap = new Zad1();
        String city = "";
        String answer = "";
        System.out.println("Enter name of the city and then its country. [\"-\" ands the input]");
        do{
            System.out.println("--------------------");
            System.out.print("Please enter city name: ");
            answer=UserInput.getInput();
            if ( !answer.equals("") && !answer.equals("-")) {
                city = answer;
                System.out.print("Please enter country name: ");
                answer=UserInput.getInput();
                if (!answer.equals("") && !answer.equals("-"))
                    cityCountryMap.addNewCity(city,answer);
            }
        }while ( !answer.equals("-") );

        System.out.println("==== City -> Country ====");
        System.out.println(cityCountryMap);
    }
}
