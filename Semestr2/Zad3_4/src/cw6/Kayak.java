package cw6;

public class Kayak {

    private String name;
    private int length;
    private int width;

    public Kayak(String name, int length ,int width){
        this.name = name;
        this.length = length;
        this.width = width;
    }

    //--- getters ---
    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public int getWidth(){
        return width;
    }
    //---

    @Override
    public String toString() {
        return "Kayak: "+getName()+";\n"+
                "Size: "+getLength()+"x"+getWidth()+" [cm] (LxW)";
    }
}
