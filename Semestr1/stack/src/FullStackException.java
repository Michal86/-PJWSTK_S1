/**
 * User: Michał Radzewicz
 * Date: 2018-02-07
 */
public class FullStackException extends Exception {
    public FullStackException(){
        System.err.println("Stack if already full!");
    }
}
