package Learn.genericTypes;

import java.util.Random;

public class RandomT {

    public static <T> Box createTbox(String boxName) {
        Random rand = new Random();
        Box box = new Box(boxName);
        String tmpStr;
        Integer tmpInt;

        for (int i = 0; i < 2; i++) {
            int los = rand.nextInt(2);

            if (los == 0) {
                tmpStr = randStr();

                if (i==0)
                    box.setItem1(tmpStr);
                else
                    box.setItem2(tmpStr);

            } else if (los == 1){
                tmpInt = rand.nextInt(2);

                if (i==0)
                    box.setItem1(tmpInt);
                else
                    box.setItem2(tmpInt);
            }

        }
        return box;
    }

    private static String randStr(){
        Random rand = new Random();
        String chars = "ab", finalStr = "";
        char c;
        for (int i = 0; i <2 ; i++) {
            c = chars.charAt(rand.nextInt(chars.length()));
            finalStr += c;
        }
        return finalStr;
    }

}
