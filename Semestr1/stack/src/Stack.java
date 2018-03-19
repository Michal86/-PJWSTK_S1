import java.util.EmptyStackException;

import static java.lang.System.out;
import static java.lang.System.setOut;

/**
 * User: Michał Radzewicz
 * Date: 2018-02-07
 * Stack uses LIFO (Last In First Out) strategy.
 */

public class Stack {
    private Object[] tab;
    private int count;

    public Stack(int size) throws CreateStackException{
        if (size <= 0) throw new CreateStackException();

        this.tab = new Object[size];
        this.count = 0;
    }

    public void push(Object obj) throws FullStackException{
        if (tab.length>count){
            tab[count++] = obj;
        } else
            throw new FullStackException();
    }//END OF push

    public Object pop() throws MyEmptyStackException{
        if (count <=0) throw new MyEmptyStackException();

        return tab[--count];
    }//END OF pop

    public void clear(){
        for (Object obj : tab) {
            obj = null;
        }
        this.count = 0;
    }

    public void displayStack(){
        System.out.println("==========");
        for(int i=count-1; i>=0; i--) {
            System.out.println(i+1 +": "+ tab[i]);
        }
        System.out.println("==========\n");
    }//END OF displayStack()

    public Object nthObj(int index){
        return tab[--index];
    }

    public int getCount() {
        return this.count;
    }

    public int getSize(){
        return this.tab.length;
    }
}
