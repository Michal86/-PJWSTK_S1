package cw6;
/**
 * Stwórz 2 klasy:
 -	public class Ferrari
 -	public class Mclaren
 Wszystkie te klasy implementują interfejs Auto w którym są metody:
 -	public String getName
 -	public int getPower
 -	public double getAccel
 Które po kolei zwracają nazwe auta, moc, i przyśpieszenie. Oraz stwórz klasę Kayak z metodami:
 -	public String getName
 -	public int getSize
 Korzystając z typów generycznych stwórz klasę Garaz która będzie przechowywać obiekty i będzie możliwość wypisania jaki ma obiekt,
 wyprowadzenia obiektów oraz wprowadzania obiektów. Utwórz obiekt Garaz i po kolei wprowadź, wypisz co w nim jest
 i wyprowadź obiekty wcześniej stworzonych klas.
 */
public class Main {

    public static void main(String[] args){
        Garage myGarage = new Garage();
        //--- BUY few cars and ---
        myGarage.addGarageObj(
                new Ferrari("Ferrari 812",800,2.9d)
        );
        myGarage.addGarageObj(
                new Mclaren("Senna", 789, 3.2d)
        );
        //--- and Kayak, because why not.. ---
        myGarage.addGarageObj(
                new Kayak("Prijon Cruiser II",470,73)
        );
        //--- display what's inside ---
        myGarage.listGarageObjects();

        //--- take out things ---
        System.out.println("=============================");
        System.out.println("Clear garage -> reverse order");
        System.out.println("=============================");
        myGarage.clearGarage();
        myGarage.listGarageObjects();

    }
}
