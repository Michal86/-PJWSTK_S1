package Learn.innerClasses;

public class OuterClass {
    //OUTER CLASS - can access private int tmp of its INNER CLASS
    private static int xStPrv = 5;
    public static int xStPub = 15;

    private String name;
    private int xPrv = 10;
    public int xPub = 20;

    //INNER CLASS - it can access private fields of OuterClass!
    class InnerClass1{
        private int tmp;
        //private static int err = 6;//CAN NOT DECLATE STATIC
        public InnerClass1(int tmp){
            this.tmp = tmp;
        }

        public void accessTest(){
            System.out.println("=== DISPLAYs FROM INNER ===");
            System.out.println("Outer class fields:");
            System.out.println("static private x: "+xStPrv);
            System.out.println("static public x: "+xStPub);
            System.out.println("private x: "+xPrv);
            System.out.println("public x: "+xPub+"\n---");
            System.out.println("Inner class fields:");
            System.out.println("private tmp: "+tmp);
            int a = OuterClass.xStPrv;
            System.out.println("int a = OuterClass.xStPrv: "+a);
        }
    }


    //STATIC NESTED CLASS
    static class NestedClass1{
        private int xPrivNested = 222;
        public int xPubNested = 333;

        private static int xPrvStaticNested = 444;
        public static int xPubStaticNested = 555;

        private NestedClass1(){
        }

        //Can access only Outer static fields!
        private void accessTest(){
            System.out.println("Changing OUTER xStPrv, xStPub from static nested class");
            xStPrv = 1;
            xStPub = 2;
        }
    }

    //====================================================================
    public void mainAccessTestInner(){
        System.out.println("\n=== CHANGE InnerClass tmp FROM OUTER ===");
        InnerClass1 inner = new InnerClass1(3);
        inner.tmp = 12;
        inner.accessTest();
    }

    public void mainAccessTestNested(){
        System.out.println("\n=== CHECK NESTED CLASS from OUTER ===");
        System.out.println("Main static int xStPrv: "+ xStPrv);
        System.out.println("Main static int xStPub: "+ xStPub);
        NestedClass1 nestedC = new NestedClass1();
        nestedC.accessTest();
        System.out.println("Main static int xStPrv: "+ xStPrv);
        System.out.println("Main static int xStPub: "+ xStPub);
        //---
        System.out.println("Display all from nested:");
        System.out.println(nestedC.xPubNested);
        System.out.println(nestedC.xPrivNested);
        System.out.println("NestedClass1.xPrvStaticNested: " +NestedClass1.xPrvStaticNested);
        System.out.println("NestedClass1.xPubStaticNested: " +NestedClass1.xPubStaticNested);
    }
    //====================================================================

}
