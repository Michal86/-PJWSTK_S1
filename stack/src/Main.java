/**
 * User: Michał Radzewicz
 * Date: 2018-02-07
 * Testing Stack class
 */
public class Main {
    public static void main(String[] args){
        try {
            Stack myStack = new Stack(4);
            System.out.println("Size of the stack: "+ myStack.getSize());

            System.out.println("> 2 pushes");
            myStack.push("first");
            myStack.push("Second");
            myStack.displayStack();

            System.out.println("> 2 additional pushes");
            myStack.push(3);
            myStack.push(4.12);
            myStack.displayStack();

            System.out.println("> 1 pop");
            myStack.pop();
            myStack.displayStack();

        }
        catch (CreateStackException e){
            e.printStackTrace();
        }
        catch (MyEmptyStackException e){
            e.printStackTrace();
        }
        catch (FullStackException e){
            e.printStackTrace();
        }
    }
}
