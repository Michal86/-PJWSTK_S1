package main.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TheModel implements Storable{

    private StringBuilder    allMessages;
    private SimpleDateFormat sim;

    public TheModel(){
        this.allMessages = new StringBuilder();
        sim = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    }

    //--- getters & setters ---
    @Override
    public void storeMsg(String msg) {
        this.allMessages.append(sim.format(new Date())).append(" > ")
                .append(msg).append("\n");
    }

    @Override
    public StringBuilder read() {
        return allMessages;
    }
}
