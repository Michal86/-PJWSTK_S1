package zad1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* zad. 1
Stwórz klase abstrakcyjna z zaimplementowanym interfejsem do sortowania kolekcji,
w tej klasie mają być metody abstrakcyjne które docelowo mają zwracać argumenty oraz metody zmieniające argumenty,
powinny być też dwa stringi według których będzie sortowane (pierwszy jako główny drugi w przypadku gdy pierwsze będą takie same)
stwórz też typ wyliczeniowy. Stwórz klasę dziedziczącą która wypełni w konstruktorze argumenty i uzupełni metody.
*/
public abstract class SortingManager implements Comparable<SortingManager>{

    public String name;
    public String surname;
    public Gender gender;
    public enum Gender {
        M,
        F
    }

    public SortingManager(String name, String surname, Gender gender){
        this.name    = name;
        this.surname = surname;
        this.gender  = gender;
    }

    //--- getters & setters ---
    public abstract String getName();
    public abstract String getSurname();
    public abstract void setName(String name);
    public abstract void setSurname(String surname);

    //-- for comparison ---
    @Override
    public int compareTo(SortingManager sortingAgainst) {
        if (this.name.equals(sortingAgainst.getName())){
            return this.surname.compareTo(sortingAgainst.getSurname());
        }
        else
            return this.name.compareTo(sortingAgainst.getName());
    }

}
