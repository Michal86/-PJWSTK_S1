/**
 * User: Michał Radzewicz
 * Date: 2018-02-07
 */
public class CreateStackException extends Exception{
    public CreateStackException(){
        System.err.println("Can not create stack with 0 or less elements!");
    }
}
