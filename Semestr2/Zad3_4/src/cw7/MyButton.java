package cw7;

import javax.swing.*;
import java.awt.*;

public class MyButton{
    static Font myFont = new Font("Arial", Font.BOLD,12);
    private JButton button;
    private AppFrame appFrame;
    public enum ACTION{
        NUMBER(),
        CALCULATION(),
        CLEAR(),
        DOT
    }
    private ACTION type;

    public MyButton(String name, AppFrame appFrame, ACTION type, int x, int y, int width, int height){
        this.appFrame = appFrame;
        button = new JButton(name);
        this.type = type;
        button.setBounds(x,y,width,height);
        button.setFont(myFont);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        appFrame.myFrame.add(button);
        button.addActionListener(appFrame);
    }

    public JButton getButton(){
        return button;
    }

    public ACTION getBtnType() {
        return type;
    }

    public char getChar(){
        return button.getText().charAt(0);
    }

}
