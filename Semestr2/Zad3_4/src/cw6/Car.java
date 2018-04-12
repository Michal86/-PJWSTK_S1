package cw6;

import java.text.DecimalFormat;

public class Car implements Auto{

    private String name;
    private int power;
    private double accel;
    private static DecimalFormat df2 = new DecimalFormat(".##");    //format to two decimals

    public Car(String name, int power, double accel){
        this.name = name;
        this.power =power;
        this.accel = accel;
    }


    //---- interface methods ---
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public double getAccel() {
        return accel;
    }
    //---

    @Override
    public String toString() {
        return  "Name: "+getName()+";\n"+
                "Power: "+getPower()+" [hp];\n"+
                "Acceleration: "+df2.format(getAccel())+"s [0-100km/h]\n"+
                "------------------------------------------";
    }
}
