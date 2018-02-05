package zadania.regex.dodatkowe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberValidator {
    //private static final String NUMBER_PATTERN = "^-?\\d*[,\\d+]*[\.\\d]*$";     //no groups, trying with ,
    //private static final String NUMBER_PATTERN = "^-?\d+(,\d+)*(\.\d+(e\d+)?)?$"; //1.2e10
    private static final String NUMBER_PATTERN = "^^-?\\d*[\\.\\d+]*$";
    Pattern pattern;
    Matcher matcher;

    public NumberValidator(){
        this.pattern = Pattern.compile(NUMBER_PATTERN);
    }

    public boolean checkNumber(String number){
        matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public double parseToNumber(String number){
        Double result = 0.0;
        if( checkNumber(number) ){
            if (!number.contains(".")) {
                Integer resultInt; //only to display/proof it's int
                resultInt = Integer.parseInt(number);
                System.out.println("Parsed: "+ resultInt + " ->" +resultInt.getClass().getSimpleName());
                result = (double) resultInt;
            }
            else {
                result = Double.parseDouble(number);
                System.out.println("Parsed: "+ result+ " ->" +result.getClass().getSimpleName());
            }

        }
        //-------------
        return result;
    }
}
