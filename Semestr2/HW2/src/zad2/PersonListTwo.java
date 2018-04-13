package zad2;
import zad1.Person;
import zad1.SortingManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersonListTwo {

    private List<Person> personList;

    public PersonListTwo(){
        personList = new ArrayList<Person>();
    }
    //--- Just to get my pre prepared list---
    public PersonListTwo(List<Person> personList){
        this.personList = new ArrayList<>(personList);
    }

    //--- To add new Perswon to the list ---
    public void addNewPerson(Person newPerson){
        personList.add(newPerson);
    }

    //--- getter ---
    public List<Person> getPersonList() {
        return personList;
    }


    //==========================================
    public static void main(String[] args){
        PersonListTwo mySecondlist = new PersonListTwo(ListOfDefinedPeople.fewPeople);

        System.out.println("Club members:");
        mySecondlist.getPersonList().forEach(p-> System.out.println(p));
        System.out.println("============================");
        //--- ADD ANONYMOUS ---
        Person anonymousPerson1 = new Person("Adam", "Nowy", SortingManager.Gender.M){
            @Override
            public String toString(){
                return "Anonymous ->"+super.toString();
            }
        };
        Person anonymousPerson2 = new Person("Agata", "Nowy", SortingManager.Gender.F){
            @Override
            public String toString(){
                return "Anonymous ->"+super.toString();
            }
        };
        //--- CHECK LIST ---
        mySecondlist.addNewPerson(anonymousPerson1);
        mySecondlist.addNewPerson(anonymousPerson2);
        System.out.println("Club members:");
        mySecondlist.getPersonList().forEach(p-> System.out.println(p));
        System.out.println("============================");
        //--- SORT LIST ---
        Collections.sort(mySecondlist.getPersonList());
        System.out.println("--- SORTED members ---");
        mySecondlist.getPersonList().forEach(p-> System.out.println(p));
    }//END_OF_MAIN
}
