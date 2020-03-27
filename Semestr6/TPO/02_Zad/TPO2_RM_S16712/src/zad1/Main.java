/**
 *
 *  @author Radzewicz Michał S16712
 *
 */

package zad1;


public class Main {
  public static void main(String[] args) {
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("Warsaw");
    Double rate1 = s.getRate("USD");
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI
/*    System.out.println(rate1);
    System.out.println(rate2);
    System.out.println(weatherJson);
    */

    GUI gui = new GUI(s);
    new Thread(()-> gui.run()).start();
  }
}
