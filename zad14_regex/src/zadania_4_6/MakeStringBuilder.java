package zadania_4_6;
/*
* MakeStringBuilder is creating StringBuilder
* Method getStringBuilder() returns this reference
*/
import java.io.FileInputStream;

public class MakeStringBuilder {
    private StringBuilder stringBuilder;

    public  MakeStringBuilder(String fileName) throws Exception{
        FileInputStream fis = new FileInputStream(fileName);
        int content = 0;

        while( (content=fis.read()) != -1 ){
            setStringBuilder(content);
        }
        fis.close();
    }
    //--- setter ---
    public void setStringBuilder(int content){
        if ( stringBuilder == null){
            stringBuilder = new StringBuilder();
        }
        this.stringBuilder.append( (char)content );
    }
    //--- getter ---
    public StringBuilder getStringBuilder(){
        return this.stringBuilder;
    }

}
