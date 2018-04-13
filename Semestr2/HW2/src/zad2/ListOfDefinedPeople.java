package zad2;

import zad1.Person;
import zad1.SortingManager;

import java.util.List;
import static java.util.Arrays.asList;

public class ListOfDefinedPeople {

    public static List<Person> fewPeople = asList(
          new Person("Adam", "Nowak", SortingManager.Gender.M),
          new Person("Bartosz", "Cmyk", SortingManager.Gender.M),
          new Person("Adam", "Adamowicz", SortingManager.Gender.M),
          new Person("Anna", "Nowak", SortingManager.Gender.F),
          new Person("Beata", "Adamczyk", SortingManager.Gender.F),
          new Person("Helena", "Nowa", SortingManager.Gender.F)
    );

    //--- to get n-th Person ---
    public String getName(int n){
        return fewPeople.get(n).getName();
    }
}
