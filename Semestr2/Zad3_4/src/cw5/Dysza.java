package cw5;

import java.util.concurrent.CountDownLatch;

public abstract class Dysza implements Runnable{

    private Thread thread;
    private CountDownLatch latch;
    private volatile boolean isRunning;
    private String typeName;
    private int sleepTimer;
    private int workingTimer;

    public Dysza(String typeName, CountDownLatch latch, int sleepTimer){
        this.typeName = typeName;
        this.latch = latch;
        this.sleepTimer = sleepTimer*1000;
        isRunning = false;
        start();
    }

    private synchronized void start(){
        if (isRunning)
            return;

        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    protected synchronized void stopWorking(){
        if (!isRunning)
            return;

        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public boolean shouldWorkChecker(long start, long deadline){
     /*   if (System.currentTimeMillis() - start>= 1000) {
            start += 1000;
                System.out.println(getTypeName()+" resume for " + ((deadline - start)/1000)+ " sek");
        }*/
        if (deadline > System.currentTimeMillis())
            return true;
        else
            return false;
    }

    //----
    @Override
    public void run(){
        //isRunning = true;
        while (isRunning){
            try {
                latch.await();
                //---
                System.out.println(this.getTypeName()+" -> is working.");
                long now = System.currentTimeMillis();
                long workUntil = now + getWorkingTimer();
                while(shouldWorkChecker(now, workUntil)) {
                    workingState();
                }
                //---
                synchronized (this) {
                    System.out.println("## "+this.getTypeName() + " -> sleeping. ##");
                }
                Thread.sleep(getSleepingTime());
            } catch (InterruptedException e){
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        //--

    }

    //--- getters ---
    public String getTypeName() {
        return typeName;
    }

    public int getSleepingTime(){
        return sleepTimer;
    }

    public int getWorkingTimer() {
        return workingTimer;
    }

    public Thread getThread() {
        return thread;
    }

    //---- setters ---
    public void setWorkingTime(int workingTimer) {
        this.workingTimer = workingTimer*1000;
    }

    public abstract void workingState();
}
