package cw1;

import java.util.Scanner;

public class UserInput {
    private static Scanner reader = new Scanner(System.in);

    public static String getInput(){
        String ui = reader.nextLine();

        if ( ui.matches(".*\\d+.*") ){
            System.out.println("Please insert words only.");
            return ui = "";
        }
        return ui;
    }
}
