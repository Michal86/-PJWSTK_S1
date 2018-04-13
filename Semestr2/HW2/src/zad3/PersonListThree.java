package zad3;

import zad1.Person;
import zad2.ListOfDefinedPeople;

import java.util.*;
import java.util.stream.Collectors;

public class PersonListThree {

    private Map<String, Person> myMap;
    private Stack<Person> myStack;

    public PersonListThree(){
        myMap = convertPeopleToMap(ListOfDefinedPeople.fewPeople);
        myStack = convertPeopleToStack(ListOfDefinedPeople.fewPeople);
    }

    //--- To Map ---
    private Map<String, Person> convertPeopleToMap(List<Person> someList){
        Map<String,Person> tmpMap = someList.stream().collect(
                Collectors.toMap( x -> "ID-"+ (someList.indexOf(x)+1), x->x) );

        return tmpMap;
    }

    //--- To Stack ---
    private Stack<Person> convertPeopleToStack(List<Person> someList){
        Stack<Person> tmpStack = new Stack<>();
        someList.forEach(p-> tmpStack.push(p));

        return tmpStack;
    }


    //==========================================
    public static void main(String[] args) {
        PersonListThree myList3 = new PersonListThree();

        System.out.println("=== MY MAP ===");
        myList3.myMap.forEach((k,v) -> System.out.printf("%s = %s%n", k, v));

        System.out.println("=== MY STACK ===");
        myList3.myStack.forEach(v -> System.out.println(v));
    }
}
