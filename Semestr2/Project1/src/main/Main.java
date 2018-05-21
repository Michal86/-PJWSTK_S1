package main;

import main.controller.TheController;
import main.model.TheModel;
import main.view.InputView;
import main.view.MainView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Michał Radzewicz
 * Date: 2018-04-22
 * Projekt z wykorzystaniem wzorca MVC. Obraz oraz model, obsługiwane są za pomocą kontrolera.
 * Brak zależności modelu od widoków.
 *
 * Klasa TheView - służy jako wzorzec dla okien i jest dziedziczona przez klasy MainView oraz InputView.
 * Klasa TheController - mój kontroler, który obsługuje przyciski, aktualizacje dane modelu oraz odświeża widoki
 * Klasa TheModel - mój model, przechowuję tam wpisane dane, które implementują mój interfejs Storable.
 *
 */

public class Main {

    public static void  main(String args[]){
        System.out.println(Thread.activeCount());
        SwingUtilities.invokeLater(
            Main::setUp
        );
    }

    private static void setUp() {
        MainView myView = new MainView(
                "Projekt S16712", "START", "Opis", "");
        TheModel myModel = new TheModel();
        List<InputView> viewList = new ArrayList<>() ;
        TheController myController = new TheController(myView, myModel, viewList);
    }

}
