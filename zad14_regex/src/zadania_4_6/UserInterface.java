package zadania_4_6;

import java.util.Scanner;

public class UserInterface {
    private Scanner reader;
    private RegexChecker regToCheck;

    public UserInterface(RegexChecker regToCheck){
        this.reader = new Scanner(System.in);
        this.regToCheck = regToCheck;
    }

    public void start(){
        //displayMenu();
        String command = null;
        boolean run = true;

        while(run){
            displayMenu();
            //Aded to solve problem with new lines which triggered handleCommand
            reader = new Scanner(System.in);
            System.out.print("Command >");
            command = reader.nextLine();

            if (command.equals("end")){
                run = false;
                System.out.println("Exiting, thanks!");
            }
            else{
                handleCommand(command);
                System.out.print("");
            }
        }
        reader.close();
    }//end of start

    public void handleCommand(String command){
        if( command.equals("1")){
            System.out.println("Wprowadz date w formacie [mm/dd/yyyy]: ");
            System.out.print("miesiac: ");
            int mies = reader.nextInt();
            System.out.print("dzien: ");
            int dzien = reader.nextInt();
            System.out.print("rok: ");
            int rok = reader.nextInt();
            regToCheck.displayWiadomosc(mies,dzien,rok);
        }
        if( command.equals("2")){
            System.out.print("Wprowadz numer dnia: ");
            int dzien = reader.nextInt();
            regToCheck.displayWiadomosc(-1,dzien,-1);
        }
        if( command.equals("3")){
            System.out.print("Wprowadz numer miesiaca: ");
            int miesiac = reader.nextInt();
            regToCheck.displayWiadomosc(miesiac,-1,-1);
        }
        if( command.equals("4")){
            System.out.print("Wprowadz adres IP w formacie [0-255.0-255.0-255.0-255] : ");
            String ip = reader.nextLine();
            regToCheck.displayWiadomoscIp(ip);
        }

    }//end of handleCommand

    private void displayMenu(){
        System.out.println("MENU:");
        System.out.println("[1] - Znajdz log z zadanej daty.");
        System.out.println("[2] - Wyswietlenie wszystkich wiadomosci z zadanego dnia.");
        System.out.println("[3] - Wyswietlenie wszystkich wiadomosci z zadanego miesiaca.");
        System.out.println("[4] - Wyswietlenie wszystkich wiadomosci z zadanego adresu ip.");
        System.out.println("[end] - Zakoncz dzialanie.");
        System.out.println("---------------------------");
    }
}
