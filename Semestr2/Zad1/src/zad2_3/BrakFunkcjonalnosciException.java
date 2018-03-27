package zad2_3;

class BrakFunkcjonalnosciException extends Exception{
    public BrakFunkcjonalnosciException() {
        System.err.println("Ten model nie posiada tej funkcjonalności.");
    }
}
