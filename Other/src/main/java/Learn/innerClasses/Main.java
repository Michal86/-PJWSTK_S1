package Learn.innerClasses;

import java.util.HashMap;
import java.util.Map;

/*
Note to remember:
- Nested interfaces are static by default.
    You don’t have to mark them static explicitly as it would be redundant.
- Nested interfaces declared inside class can take any access modifier,
    however nested interface declared inside interface is public implicitly.
*/

public class Main {
    public static void main(String[] args){

        OuterClass outerClass = new OuterClass();
        OuterClass.InnerClass1 innerClass1 = outerClass.new InnerClass1(7);
        innerClass1.accessTest();
        outerClass.mainAccessTestInner();
        //---
        OuterClass.InnerClass1 innerClass2 = outerClass.new InnerClass1(123);
        outerClass.xPub = 999; //<- this will change x display in both INNERS
        //They hold reference to outerClass
        innerClass1.accessTest();
        innerClass2.accessTest();
        //---

        outerClass.mainAccessTestNested();
        int check = OuterClass.NestedClass1.xPubStaticNested;//Can not access private static from here
        System.out.println(check);

        //=== Test-> using implemented inner Map interface ===

        System.out.println("\nTest with Map.Entry - Entry interface inside Map interface.");
        Map<String, Integer> daysInMonths = new HashMap<>();
        daysInMonths.put("styczeń", 31);
        daysInMonths.put("luty", 28);
        daysInMonths.put("marzec", 31);

        for (Map.Entry<String, Integer> curent : daysInMonths.entrySet()){
            System.out.println(curent.getKey() + " - " + curent.getValue()+ " dni.");
        }
    }
}
