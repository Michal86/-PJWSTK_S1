/*
@ Michał Radzewicz
1. Sekwencje znaków - hasła, które od lewej do prawej składają się z:
   3 cyfr następujących po sobie,
   4 liter (alfabetu angielskiego) następujących po sobie oraz z jednego lub więcej znaków ze zbioru
     {*, ^, %, #, ~, !, &, |, @, $}.
-----------------------------------------------------------------------
2. Ciągi zero-jedynkowe nieparzystej długości, np. 101, 0, 01010, ...
-----------------------------------------------------------------------
3. Słowa składające się z liter alfabetu polskiego, w których występują litery w porządku alfabetycznym
   (nie rozróżniamy małych i dużych liter), np. "abc", "nOs", "bór", "cĘgi", "aby", "chŁop", ...
-----------------------------------------------------------------------
4. Dowolne liczby (całkowite, rzeczywiste).
   Przykłady liczb: -29.30, -12, -0.343536, 0.2, .2 (jako 0.2), 11, 13.14, -.5 (jako -0.5).
-----------------------------------------------------------------------
5. Dany jest plik tekstowy zawierający kod programu w Javie.
   Napisać program, który czyta z kodu wszystkie wywołania metody
   Math.max(double,double), w których jako argumenty występują
   wyłącznie identyfikatory lub literały. Zapisać do pliku
   wyjściowego, podając informacje o argumentach w/w wywołania.
   Np. dla wywołania w kodzie:... m = Math.max(m , 4.5) ...informacja wyjściowa powinna być (m, 4.5).
-----------------------------------------------------------------------
6. Zbudować wyrażenie regularne, które wykryje te same słowa, które występują po sobie w danym tekście,
   np. "mamy" w "Dziś mamy mamy weekend", "będzie" w "Jutro będzie będzie poniedziałek", ...
-----------------------------------------------------------------------
NOTE for the future -> WOULD BE BETTER TO CREATE ONE CLASS FOR REGEX AND EXTEND IT.
*/
package zadania.regex.dodatkowe;

import java.util.ArrayList;
import java.util.Scanner;

public class MainDodatki {
    private static Scanner reader;
    private static ArrayList<Double> liczby;    //to store numbers for ex. 4

    public static void main(String[] args){
        reader = new Scanner(System.in);
        String answer = "";
        try {
            while(!answer.equalsIgnoreCase("end")){
                info();
                answer = reader.nextLine();
                if ( answer.equals("1")){
                    //===== EXERCISE 1 =====
                    checkPass();
                }else if(answer.equals("2")){
                    //===== EXERCISE 2 =====
                   checkSequences();
                }else if(answer.equals("3")){
                    //===== EXERCISE 3 =====
                    checkPlWords();
                }
                else if(answer.equals("4")){
                    //===== EXERCISE 4 =====
                    checkNumber();
                    //just to test my code
                    double suma = 0.0;
                    for (Double d : liczby){
                        suma += d;
                    }
                    System.out.println("Sum of the digits "+liczby);
                    System.out.println("is equal to "+ suma);
                }
                else if(answer.equals("5")) {
                    //===== EXERCISE 5 =====
                    checkFiles(1);
                }
                else if(answer.equals("6")) {
                    //===== EXERCISE 6 =====
                    checkFiles(2);
                }
            }
            System.out.println("Bye!");
        }
        catch (Exception e){
            System.err.println(e);
        }
    }//END OF MAIN

    //Using static methods, just to practice
    static void checkFiles(int index) throws Exception {
        if (index == 1){
            FileOperations.handleFile("./src/zadania/regex/dodatkowe/mathMaxJava.txt");
            MathMaxValidator validate = new MathMaxValidator(FileOperations.getStrBld());
        }
        else if(index ==2){
            FileOperations.handleFile("./src/zadania/regex/dodatkowe/doubleWords.txt");
            DoubleWordsValidator validate = new DoubleWordsValidator(FileOperations.getStrBld());
        }
        System.out.print("Do You want to display stored file string? [yes-> y /no-> any key]: ");
        if ( reader.nextLine().equals("y") ){
            FileOperations.display();
        }
    }

    static void checkNumber(){
        boolean repeat = true;
        NumberValidator numberValidator = new NumberValidator();
        String yourNumber = "";
        liczby = new ArrayList<>();
        //--------------------------
        System.out.println("Ex. 4");
        while (repeat) {
            menu(4);
            yourNumber = reader.nextLine();
            repeat = numberValidator.checkNumber(yourNumber);
            //troche mylace, dodam liczbe jezeli przeszla test regex[double|int]
            if (repeat){
                liczby.add(numberValidator.parseToNumber(yourNumber));
            }

            repeat = isValid(yourNumber, repeat);
        }
    }//END OF checkNumber

    static void checkPlWords(){
        boolean repeat = true;
        PLWordValidator wordValidator = new PLWordValidator();
        String yourWord = "";
        //--------------------------
        System.out.println("Ex. 3");
        while (repeat) {
            menu(3);
            yourWord = reader.nextLine();

            repeat = isValid(yourWord, wordValidator.checkWord(yourWord) );
        }
    }//END OF checkPlWords

    static void checkSequences(){
        boolean repeat = true;
        ZeroOneValidator sequenceValidator = new ZeroOneValidator();
        String yourSequence = "";
        //--------------------------
        System.out.println("Ex. 2");
        while (repeat) {
            menu(2);
            yourSequence = reader.nextLine();

            repeat = isValid(yourSequence, sequenceValidator.checkSequence(yourSequence) );
        }
    }//END OF checkSequences

    static void checkPass(){
        boolean repeat = true;
        PasswordValidator passwordValidator = new PasswordValidator();
        String yourPassword = "";
        //--------------------------
        System.out.println("Ex. 1");
        while (repeat) {
            menu(1);
            yourPassword = reader.nextLine();

            repeat = isValid(yourPassword, passwordValidator.validate(yourPassword) );
        }
    }//END OF checkPass

    //===== METHODS TO DISPLAY/GET USER ANSWERS =====
    static boolean isValid(String stringInput, boolean test){
        if ( test ==true ){
        System.out.println("\n" +stringInput+ " is valid!");
        }
        else {
        System.out.println("\n" +stringInput+ " is NOT valid!");
        }

        System.out.print("Try again? [yes-> y /no-> any key]: ");
        if ( !reader.nextLine().equals("y") )
            return false;
        else
            return true;
    }//END OF isValid()

    static void menu(int which){
        String word = "", statement = "";
        switch ( which ) {
            case 1:
                word = "Password";
                statement = "> (3 digits) + (4 letters [eng only]) + (1 special char: *, ^, %, #, ~, !, &, |, @, $)";
                break;
            case 2:
                word = "Zero-one sequence";
                statement = "> (0/1 [odd sequences only]); e.g. 101, 1, 0...";
                break;
            case 3:
                word = "PL word";
                statement = "> (2+ chars) [Alphabetical order]; e.g. \"abc\", \"nOs\", \"bór\"";
                break;
            case 4:
                word = "Number";
                statement = "> (int or real); e.g -0.343536, 0.2, .2 (as 0.2), 11, 13.14, -.5 (as -0.5)";
                break;
        }

            System.out.println("["+word+" RULES]");
            System.out.println(statement);
            System.out.println("----------------------------------------------------------------------------------------");
            System.out.print("Insert Your " +word+ ": ");

    }
    static void info(){
        System.out.println("Select number [1-5] to validate: ");
        System.out.print("[1]. Password\t");
        System.out.println("[2]. [0,1] sequences");
        System.out.print("[3]. PL words with alphabetical letter order\t");
        System.out.println("[4]. Number [real, integer]");
        System.out.print("[5]. Finds inside the file Math.max() methods\t");
        System.out.println("[6]. Checking for Doubled Words inside the file");
        System.out.print("[Exit]. Type \"end\"  -> ");
    }
}
