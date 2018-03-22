package Learn.genericTypes;

//Testing generic types
//Box==Box is they have same types and values inside, no matter the position
public class Main {

    public static void main(String args[]){
        Box tBox1 = RandomT.createTbox("Box 1");
        Box tBox2 = RandomT.createTbox("Box 2");

        System.out.println("--- CHECK BOXes---");
        tBox1.isEqual(tBox2);
        tBox1.displayBox();
        tBox2.displayBox();

        //=================================================
        Box x = null,
            y = null;
        boolean areEqual = false;
        int counter = 0;
        while(!areEqual){
            x = RandomT.createTbox("xBox");
            y = RandomT.createTbox("yBox");
            areEqual = x.isEqual(y);
            counter++;
        }
        System.out.println("After "+counter+" random creations of Boxes.");
        x.displayBox();
        y.displayBox();

    }//main

}
