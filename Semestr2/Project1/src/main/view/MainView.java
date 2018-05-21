package main.view;

import javax.swing.*;
import java.awt.*;

public class MainView extends TheView{

    private final int WIDTH = 300;
    private final int HEIGHT = 420;
    private final Color THEME_COLOR = new Color(66,118,118);
    private final Color TEXT_COLOR = new Color(225,228,140);
    private final Color BUTTON_COLOR = new Color(59,89,125);
    //-----------------------------

    public MainView(String frameName, String buttonName, String mainBorderName, String buttonBoarderName) {
        super(frameName, buttonName, mainBorderName, buttonBoarderName);
        setBasicSettings(this, WIDTH, HEIGHT);

        //--- set description area ---
        setMsgArea("Program umożliwia następujące działania.\n\n" +
                "Po wciśnięciu przycisku \"START\", utworzone zostanie nowe okno z funkcjami opisanymi poniżej:\n\n"+
                "1. Wpisanie dowolnego tekstu w wyznaczonym polu tekstowym;\n"+
                "2. Dodanie wprowadzonego tekstu, po wcisnięciu przycisku \"DODAJ\";\n"+
                "3. Wyświetlenie nowo dodanego tekstu oraz, jeżeli takowe były, wcześniej dodanych wpisów.\n"+
                "\n\nNaciśnięcie krzyżyka głównego okna, zakończy działanie programu.");
        getMsgArea().setMinimumSize(new Dimension(280, 275));
        getMsgArea().setPreferredSize(new Dimension(280, 275));
        getMsgArea().setMaximumSize(new Dimension(280, 275));
        getMsgPanel().setBackground(THEME_COLOR);
        getMsgPanel().add(getMsgArea());
        getMsgArea().setBackground(TEXT_COLOR);

        //--- set button area ---
        getBtnPanel().setMinimumSize(new Dimension(WIDTH, 60));
        getBtnPanel().setPreferredSize(new Dimension(WIDTH, 60));
        getBtnPanel().setMaximumSize(new Dimension(WIDTH, 60));
        getBtnPanel().setBorder(BorderFactory.createEmptyBorder());
        getMainButton().setAlignmentX(Component.CENTER_ALIGNMENT);

        //--- color components ---
        setThemeColors(THEME_COLOR, THEME_COLOR, THEME_COLOR, TEXT_COLOR, BUTTON_COLOR);
    }

}
