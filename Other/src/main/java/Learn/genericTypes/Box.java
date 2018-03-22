package Learn.genericTypes;

public class Box<T, U> {

    private String boxName;
    private T item1;
    private U item2;

    public Box(String boxName){
       this.boxName = boxName;
    }

    public T getItem1(){
        return item1;
    }
    public U getItem2(){
        return item2;
    }

    public void setItem1(T item1) {
        this.item1 = item1;
    }

    public void setItem2(U item2) {
        this.item2 = item2;
    }

    public boolean isEqual(Box toCompare){
        boolean equal = true;
        if (toCompare==null) System.out.println("Not equal - delivered Box is null!");

        T tmp;
        if (item1.equals(toCompare.getItem1()) && item2.equals(toCompare.getItem2()) ||
                item2.equals(toCompare.getItem1()) && item1.equals(toCompare.getItem2())) {
            System.out.println("Boxes are equal!");
        }
        else{
            System.out.println("Boxes are not equal.");
            equal = false;
        }
        return equal;
    }

    public void displayBox(){
        System.out.println(boxName+"<"+item1.getClass().getSimpleName()+", "+item2.getClass().getSimpleName()+">");
        System.out.print("Object 1: "+ item1.toString()+",  ");
        System.out.println("Object 2: "+ item2.toString());
    }
}
