package Learn.CheckComparable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Person implements Comparable<Person> {
    private String name;
    private int salary;

    public Person(String name, int salary){
        this.name   = name;
        this.salary = salary;
    }

    public String getName() {
        return this.name;
    }

    public int getSalary() {
        return this.salary;
    }

    @Override
    public String toString() {
        return "Name: " +getName()+ ", salary: " + getSalary()+"$|";
    }

    //-------
    public int compareTo(Person toCompare) {
        return this.getName().compareToIgnoreCase(toCompare.getName());
    }
   /*
    public int compareTo(Person toCompare) {

        return toCompare.getSalary() - this.salary;
    }*/
    public static void main(String[] args){
        List<Person> students= new ArrayList<>();
        students.add(new Person("Cal", 2) );
        students.add(new Person("Ala", 4) );
        students.add(new Person("Cba", 3) );
        students.add(new Person("Bla", 5) );

        System.out.println("Original:");
        System.out.println(students);
        System.out.println("-------------------------------------");
/*        Collections.sort(students);
        System.out.println("By money status:");
        System.out.println(students);
        System.out.println("-------------------------------------");*/
        Collections.sort(students);
        System.out.println("By names:");
        System.out.println(students);
    }
}
