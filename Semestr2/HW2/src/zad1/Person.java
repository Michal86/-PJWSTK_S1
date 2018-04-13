package zad1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Person extends SortingManager{

    public Person(String name, String surname, Gender gender){
        super(name, surname, gender);
    }

    //--- getters & setters ---
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setSurname(String surname) {
        this.surname = surname;
    }

    //--- display ---
    @Override
    public String toString() {
        return "Name: "+ getName()+" surname: "+getSurname();
    }


    //==========================================
    public static void main(String[] args){
        List<Person> clubMembers = new ArrayList<Person>();
        Person testPerson1 = new Person("Adam", "Nowak", Gender.M);
        Person testPerson2 = new Person("Bartosz", "Cmyk", Gender.M);
        Person testPerson3 = new Person("Adam", "Adamowicz", Gender.M);
        clubMembers.add(testPerson1);
        clubMembers.add(testPerson2);
        clubMembers.add(testPerson3);
        //----------------------------
        System.out.println("Club members:");
        clubMembers.forEach(p-> System.out.println(p));
        System.out.println("============================");
        //--- sort ---
        Collections.sort(clubMembers);
        System.out.println("Club members sorted:");
        clubMembers.forEach(p-> System.out.println(p));
    }
}