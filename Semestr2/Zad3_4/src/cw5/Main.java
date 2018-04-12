package cw5;

import javax.swing.text.html.HTMLDocument;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * Stwórz 4 klasy:
 * - public class LewaDolnaDysza
 * - public class LewaGornaDysza
 * - public class PrawaDolnaDysza
 * - public class PrawaGornaDysza
 * Klasy te odpowiadają dyszom do latania w zbroi iron mana zadbaj,
 * by górne dysze uruchamiały się na 2 sekundy co 4 sekundy i informowały o tym użytkownika,
 * a dolne na 3 sekundy co 2 sekund i też o tym informowały.
 * Wszystkie dysze mają działać jednocześnie.
 */
public class Main {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        CountDownLatch startSignal = new CountDownLatch(1);
        List<Dysza> suitAccessories = new LinkedList<>();
        //--- idle and working times in seconds ---
        int idleGorne = 4,
            idleDolne = 2;
        int workingTimeGorne = 2,
            workingTimeDolne = 3;

        System.out.println("Press enter key to stop...");
        System.out.println("==========================");
        //--- add threads ---
        suitAccessories.add(
                new LewaDolnaDysza("Lewa dolna dysza", startSignal, idleDolne, workingTimeDolne));
        suitAccessories.add(
                new LewaGornaDysza("Lewa gorna dysza", startSignal, idleGorne, workingTimeGorne));
        suitAccessories.add(
                new PrawaDolnaDysza("Prawa dolna dysza", startSignal, idleDolne, workingTimeDolne));
        suitAccessories.add(
                new PrawaGornaDysza("Prawa gorna dysza", startSignal, idleGorne, workingTimeGorne));

        //--- run "at the same" time ---
        startSignal.countDown();


        scanner.nextLine();
        boolean threadsStopped = false;
        do {
            threadsStopped = true;
            for (Dysza d : suitAccessories) {
                Thread t = d.getThread();

                if (t.isAlive()) {
                    threadsStopped = false;
                    System.out.println("Thread " + d.getTypeName());
                    d.stopWorking();
                    System.out.println("Alive : " + t.isAlive());
                }
                synchronized (Thread.currentThread()) {
                    System.out.println("All stopped: " + threadsStopped);
                }
            }

        }while (!threadsStopped);

        System.out.println("==========================");

    }

}
