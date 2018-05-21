package main.view;

import javax.swing.*;
import java.awt.*;

public class InputView extends TheView{

    private InputView viewHolder;
    private final int WIDTH = 500;
    private final int HEIGHT = 400;
    private final Color THEME_COLOR = new Color(161,205,115);
    private final Color TEXT_COLOR = new Color(240,235,235);
    private final Color BUTTON_COLOR = new Color(63,154,130);
    //-----------------------------
    private JTextField inputField;

    public InputView(String frameName, String buttonName, String mainBorderName, String buttonBoarderName) {
        super(frameName, buttonName, mainBorderName, buttonBoarderName);
        setBasicSettings(this, WIDTH, HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //--- set display area ---
        JScrollPane jScrollPane = new JScrollPane(getMsgArea()
        );
        jScrollPane.setMinimumSize(new Dimension(WIDTH-40, 275));
        jScrollPane.setPreferredSize(new Dimension(WIDTH-40, 275));
        jScrollPane.setMaximumSize(new Dimension(WIDTH-40, 275));
        jScrollPane.setWheelScrollingEnabled(true);
        getMsgPanel().add(jScrollPane);

        //--- set button area ---
        getBtnPanel().setMinimumSize(new Dimension(WIDTH, 60));
        getBtnPanel().setPreferredSize(new Dimension(WIDTH, 60));
        getBtnPanel().setMaximumSize(new Dimension(WIDTH, 60));
        inputField = new JTextField(25);
        getBtnPanel().add(inputField);

        //--- color components ---
        setThemeColors(THEME_COLOR, THEME_COLOR, THEME_COLOR, TEXT_COLOR, BUTTON_COLOR);
    }

    public String getInputField(){
        return inputField.getText();
    }

    public void clearInput(){
        this.inputField.setText("");
    }

}
