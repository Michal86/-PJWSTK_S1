package cw5;

import java.util.concurrent.CountDownLatch;

public class LewaGornaDysza extends Dysza{

    public LewaGornaDysza(String typeName, CountDownLatch latch, int sleepTimer){
        super(typeName, latch, sleepTimer);
    }

    public LewaGornaDysza(String typeName, CountDownLatch latch, int sleepTimer, int workingTimer){
        this(typeName, latch, sleepTimer);
        setWorkingTime(workingTimer);
    }

    @Override
    public void workingState() {
        if (getWorkingTimer()==0){
            System.out.println("Please insert idle time for: "+ getTypeName());
            return;
        }

        //System.out.println(this.getTypeName()+" -> is working.");
    }

}
