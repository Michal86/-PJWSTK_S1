/**
 * User: Michał Radzewicz
 * Date: 2018-02-07
 */
public class MyEmptyStackException extends Exception{

    public MyEmptyStackException(){
        super();
        System.err.println("Stack is empty. Nothing to pop.");
    }
}
