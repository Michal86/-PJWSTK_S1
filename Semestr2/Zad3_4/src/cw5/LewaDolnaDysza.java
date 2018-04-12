package cw5;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LewaDolnaDysza extends Dysza{

    public LewaDolnaDysza(String typeName, CountDownLatch latch, int sleepTimer){
        super(typeName, latch, sleepTimer);
    }

    public LewaDolnaDysza(String typeName, CountDownLatch latch, int sleepTimer, int workingTimer){
        this(typeName, latch, sleepTimer);
        setWorkingTime(workingTimer);
    }

    //=================================
    @Override
    public void workingState() {
        if (getWorkingTimer() ==0){
            System.out.println("Please insert idle time for: "+ getTypeName());
            return;
        }

       // System.out.println(this.getTypeName()+" -> is working.");
    }

}
