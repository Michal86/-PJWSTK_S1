package Learn;

public class App
{
    public static void main( String[] args )
    {
        System.out.println("=== Runable test ===");
        RunnableComparison compare = new RunnableComparison();

        compare.r1.run();
        compare.r2.run();
    }
}

class RunnableComparison{
    Runnable r1 = new Runnable() {
        //anonymous inner class
        @Override
        public void run() {
            System.out.println("Runnable number 1");
        }
    };

    //Lambda Runnable
    Runnable r2 = () -> System.out.println("Runnable number 2");

}