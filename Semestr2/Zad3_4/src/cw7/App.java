package cw7;

import java.awt.*;

/**
 * Korzystając z swinga stwórz prosty kalkulator okienkowy z klawiaturą numeryczną.
 * Date: 2018-04-13
 */
public class App extends Canvas implements Runnable{

    private AppFrame            frame;
    private Thread              thread;
    private volatile boolean    isRunning;
    //---

    public App(){
        this.frame = new AppFrame(220,340,"Simple Calculator", this);

    }

    private synchronized void start(){
        if (isRunning) return;

        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop(){
        if (!isRunning) return;

        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    //=== App loop ===
    @Override
    public void run() {
        isRunning = true;
        this.requestFocus();

        while(isRunning){

        }//END OF WHILE
        stop();
    }

    //===================================================
    public static void main(String[] args){
        new App();
    }
}
