import org.junit.Test;

import static org.junit.Assert.*;

/**
 * User: Michał Radzewicz
 * Date: 2018-02-07
 */
public class StackTest {


    @Test
    public void push() throws FullStackException,CreateStackException  {
        Stack myStack = new Stack(3);
        myStack.push("Obj1");
        assertEquals("Obj1", myStack.nthObj(1));
        myStack.push("Obj2");
        //Check if counter works
        assertEquals(2,myStack.getCount());
    }

    @Test
    public void pop() throws MyEmptyStackException,CreateStackException,FullStackException {
        Stack myStack = new Stack(2);
        myStack.push(33);
        Object tmp =  myStack.pop();
        assertEquals(33, tmp);

        assertEquals(0,myStack.getCount());
    }
}