package main.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TheView extends JFrame{

    private JPanel bgPanel;
    private JPanel msgPanel;
    private JTextArea msgArea;
    private JPanel btnPanel;
    private JButton mainButton;

    //Set my GUI
    TheView(String frameName,  String buttonName, String mainBorderName, String buttonBoarderName){
            super(frameName);

        //--- create main panel ---
        bgPanel = new JPanel();
        bgPanel.setBorder(BorderFactory.createTitledBorder(mainBorderName));
        bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));

        //--- add top panel  ---
        msgPanel = new JPanel();
        msgPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        createMainTxtArea();
        bgPanel.add(msgPanel);

        //--- add bottom panel  ---
        btnPanel = new JPanel();
        btnPanel.setBorder(BorderFactory.createTitledBorder(buttonBoarderName));
        mainButton = new JButton(buttonName);
        btnPanel.add(mainButton);
        bgPanel.add(btnPanel);

        //-- add to JFrame ---
        this.getContentPane().add(bgPanel);

        //--- pack and set visibility ---
        this.pack();
        setVisible(true);
    }

    //------------------------------------------------
    void setBasicSettings(JFrame frame, int width, int height){
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));
        frame.setPreferredSize(new Dimension(width, height));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

    void setThemeColors(Color mainPanel, Color topPanel, Color bottomPanel, Color textArea, Color button){
        bgPanel.setBackground(mainPanel);
        msgPanel.setBackground(topPanel);
        msgArea.setBackground(textArea);
        btnPanel.setBackground(bottomPanel);
        mainButton.setBackground(button);
        mainButton.setForeground(new Color(240,235,235));
    }

    private void createMainTxtArea(){
        msgArea = new JTextArea();
        msgArea.setLineWrap(true);
        msgArea.setWrapStyleWord(true);
        msgArea.setAlignmentY(CENTER_ALIGNMENT);
        msgArea.setBackground(null);
        msgArea.setEditable(false);
        msgArea.setBorder(BorderFactory.createLoweredBevelBorder());
    }

    //------------------------------------------------
    public void addViewListener(ActionListener e){
        mainButton.addActionListener(e);
    }

    //------------------------------------------------
    public void setMsgArea(String msg){
        msgArea.setText(msg);
    }

    JTextArea getMsgArea(){
        return msgArea;
    }

    JPanel getMsgPanel(){
        return msgPanel;
    }

    JButton getMainButton(){
        return mainButton;
    }

    JPanel getBtnPanel(){
        return btnPanel;
    }
}
