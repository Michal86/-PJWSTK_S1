package zad2_3;
/*
* Mark1 nie ma funkcji latanie, repulsory ani flary
* więc przy ich wywołaniu powinien zostać podniesiony wyjątek BrakFunkcjonalnosciException.
*/
class BrakFunkcjonalnosciException extends Exception{
    public BrakFunkcjonalnosciException() {
        System.err.println("Ten model nie posiada tej funkcjonalności.");
    }
}
