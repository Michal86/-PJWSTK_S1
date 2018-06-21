package cw7;

import javax.swing.*;
import java.awt.*;

public class Display extends JPanel{

    private int myWidth;
    private int myHeight;
    private JFrame myFrame;
    private JLabel textArea;
    private Font myFont = new Font("Arial", Font.BOLD,20);

    public Display(JFrame myFrame, int width, int height){
        setLayout(null);
        this.myFrame = myFrame;
        this.myWidth = width-8;
        this.myHeight = (int)(height*0.15);
        this.setBounds(1, 1, myWidth, myHeight);
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        //---
        textArea = new JLabel();
        textArea.setSize(myWidth-4,myHeight);
        textArea.setFont(myFont);
        textArea.setHorizontalAlignment(SwingConstants.RIGHT);
        this.add(textArea);
        updateTextArea("0.0");
        myFrame.getContentPane().add(this);
    }

    public void updateTextArea(String txtUpdated) {
        textArea.setText(txtUpdated);
    }

}
