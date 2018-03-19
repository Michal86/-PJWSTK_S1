package zadania.regex.dodatkowe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathMaxValidator {
    private static final String MATH_PATTERN = "Math.max(\\([\\s\\w\\.]+,[\\s\\w\\.]+\\))";
    private StringBuilder stringBuilder;
    private Pattern pattern;
    private Matcher matcher;

    public MathMaxValidator(StringBuilder stringBuilder) throws Exception{
        this.stringBuilder = stringBuilder;
        this.pattern = Pattern.compile(MATH_PATTERN);
        this.matcher = pattern.matcher(this.stringBuilder);

        getMatches();
    }

    // Validate given regex and display
    private void getMatches(){
        int counter = 0;
        while (matcher.find() ){
            if(matcher.group().length()!=0){
                System.out.println(++counter +": "+ matcher.group(1));
            }
        }
    }

}
