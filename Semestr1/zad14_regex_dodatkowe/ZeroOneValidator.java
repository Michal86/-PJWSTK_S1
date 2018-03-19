package zadania.regex.dodatkowe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZeroOneValidator {
    private static final String SEQUENCE_PATTERN = "[01]+";
    private Pattern pattern;
    private Matcher matcher;

    public ZeroOneValidator(){
        this.pattern = Pattern.compile(SEQUENCE_PATTERN);
    }

    public boolean checkSequence(String sequence){
        this.matcher = pattern.matcher(sequence);
        if( matcher.matches() && (sequence.length()%2 != 0) ){
            return true;
        }
        else
            return false;
    }
}
