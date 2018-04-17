package cw7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AppFrame implements ActionListener {

    protected JFrame myFrame;
    protected Display display;
    private ArrayList<MyButton> buttonsList;
    private char[] operations = {'/', '*', '-', '+','='};
    //---
    private double first=0;
    private double second=0;
    private char operation =' ';
    private boolean gotFirst = false;
    private boolean gotSecond = false;
    private StringBuilder myNumber = new StringBuilder("0");
    private boolean buildNumber = false;
    private boolean gotOperation = false;
    //---

    public AppFrame(int width, int height, String title, App calculator){
        this.myFrame = new JFrame(title);
        buttonsList = new ArrayList<>();

        myFrame.setPreferredSize(new Dimension(width, height));
        myFrame.setMinimumSize(new Dimension(width, height));
        myFrame.setMinimumSize(new Dimension(width, height));
        myFrame.setResizable(false);
        myFrame.setLayout(null);
        myFrame.setLocationRelativeTo(null);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //---
        myFrame.add(calculator);
        //add display field
        myFrame.add(display = new Display(myFrame, width, height));
        //add buttons
        setMyButtons();
        //---
        myFrame.pack();
        myFrame.setVisible(true);
    }

    public int getWidth(){
        return myFrame.getWidth();
    }
    public int getHeight(){
        return myFrame.getHeight();
    }

    //--- Prepare buttons ---
    private void setMyButtons(){
        int spaceBetween =  display.getWidth()/4;
        final int SPACE = spaceBetween/5;
        int startYFrom = display.getHeight()+SPACE;
        int startXFrom = 3* spaceBetween + SPACE;
        int numbers = 9;
        int iOper = 0;
        for (int i = 17; i >0; i--) {
            if (i == 17) {
                buttonsList.add(new MyButton("" + operations[iOper++], this, MyButton.ACTION.CALCULATION, startXFrom, startYFrom, 40, 40));
                startXFrom -= spaceBetween;
                buttonsList.add(new MyButton("c", this, MyButton.ACTION.CLEAR, startXFrom, startYFrom, 40, 40));
            }
            else if (i % 4 == 0){
                startXFrom = 3* spaceBetween + SPACE;
                startYFrom += 40+SPACE;
                buttonsList.add(new MyButton(""+operations[iOper++] , this, MyButton.ACTION.CALCULATION, startXFrom, startYFrom, 40, 40));
            }
            else if(i == 3)
                buttonsList.add(new MyButton(".", this, MyButton.ACTION.DOT, startXFrom, startYFrom, 40, 40));
            else if(numbers >= 0) {
                if (numbers == 0)
                    buttonsList.add(
                        new MyButton("" + numbers--, this, MyButton.ACTION.NUMBER, startXFrom - spaceBetween, startYFrom, 80 + 2 * SPACE - 4, 40));
                else
                    buttonsList.add(
                        new MyButton("" + numbers--, this, MyButton.ACTION.NUMBER, startXFrom, startYFrom, 40, 40));
            }
            startXFrom -= spaceBetween;
        }

    }
    //---

    //--- PRESSED BUTTONS ---
    @Override
    public void actionPerformed(ActionEvent e) {
        for (MyButton button : buttonsList){
            if (button.getButton() == e.getSource()){
                //--- NUMBERS ---
                if (button.getBtnType() == MyButton.ACTION.NUMBER) {
                    if (buildNumber ) {
                        if (Double.parseDouble(myNumber.toString())==0 &&
                                button.getButton().getText().equals("0")) {
                            buildNumber = false;
                        }
                        else
                            myNumber.append(button.getButton().getText());
                    }
                    else if (!buildNumber ){
                        myNumber = new StringBuilder();
                        myNumber.append(button.getButton().getText());
                        buildNumber = true;
                    }
                }
                //--- COMMANDS FOR CALCULATIONS ---
                else if (button.getBtnType() == MyButton.ACTION.CALCULATION) {
                    if (button.getChar() == '-' && !gotFirst && !buildNumber) {
                        myNumber = new StringBuilder();
                        myNumber.append("-");
                        buildNumber = true;
                        gotOperation = false;
                    }
                    else {
                        if (!gotFirst && Double.parseDouble(myNumber.toString()) != 0) {
                            if (button.getChar() != '=') {
                                first = Double.parseDouble(myNumber.toString());
                                gotFirst = true;
                                operation = button.getChar();
                                gotOperation = true;
                                buildNumber = false;
                            }

                        }
                        else if (gotFirst) {
                            if (!gotSecond && !buildNumber && button.getChar() != '=') {
                                operation = button.getChar();
                                gotOperation = true;
                            }
                            else if (!gotSecond && buildNumber ) {
                                second = Double.parseDouble(myNumber.toString());
                                gotSecond = true;
                            }
                        }
                        if (gotFirst && gotSecond && gotOperation){
                            first = Calculation.operationResult(first, second, operation);
                            System.out.println("Result: " + first);
                            second = 0.0;
                            gotSecond = false;
                            buildNumber = false;
                            myNumber = new StringBuilder();
                            myNumber.append(first);

                            if (button.getChar() == '=') {
                                operation = ' ';
                                gotOperation = false;
                            }
                            else {
                                operation = button.getChar();
                                gotOperation = true;
                            }
                        }

                    }
                }
                //--- COMMANDS FOR DOUBLE ---
                else if (button.getBtnType() == MyButton.ACTION.DOT){
                    if (!myNumber.toString().contains(".") && buildNumber)
                        myNumber.append(".");
                    else if (myNumber.toString().equals("0.0"))
                        myNumber.append("0.");
                }
                //--- CLEAR COMMANDS  ---
                else if (button.getBtnType() == MyButton.ACTION.CLEAR) {
                    clearNumbers();
                }
            }

        }
        String mySNumber = String.valueOf(myNumber);
        display.updateTextArea(mySNumber);

    }
    private void clearNumbers(){
        myNumber = new StringBuilder("0.0");
        first = 0;
        second = 0;
        operation = ' ';
        gotFirst = false;
        gotSecond = false;
        gotOperation = false;
        buildNumber = false;
    }
}
