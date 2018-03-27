package zad1;

class OblodzenieException extends Exception{

    public OblodzenieException(String oblodzenie) {
        System.err.println("Możliwe oblodzenie! Leć niżej!");
    }

}
