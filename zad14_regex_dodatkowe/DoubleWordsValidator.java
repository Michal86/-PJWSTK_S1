/*
@ Michał Radzewicz - great help from thi web site https://www.regular-expressions.info/backref.html
@ This source helped me to understand backreference.
    "Here's how: <([A-Z][A-Z0-9]*)\b[^>]*>.*?</\1>.
    This regex contains only one pair of parentheses, which capture the string matched by [A-Z][A-Z0-9]*.
    This is the opening HTML tag. (Since HTML tags are case insensitive, this regex requires case insensitive matching.)
    The backreference \1 (backslash one) references the first capturing group.
    \1 matches the exact same text that was matched by the first capturing group."
*/

package zadania.regex.dodatkowe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoubleWordsValidator{
    private static final String DOUBLE_PATTERN = "(\\p{L}{2,})\\s+\\1"; //group 1: (\p{L}{2,}), \1 : matches same text captured in group 1
    private StringBuilder stringBuilder;
    private Pattern pattern;
    private Matcher matcher;

    public DoubleWordsValidator(StringBuilder stringBuilder) throws Exception{
        this.stringBuilder = stringBuilder;
        this.pattern = Pattern.compile(DOUBLE_PATTERN);
        this.matcher = pattern.matcher(this.stringBuilder);

        getMatches();
    }

    // Validate given regex and display
    private void getMatches(){
        int counter = 0;
        while (matcher.find() ){
            if(matcher.group().length()!=0){
                System.out.println(++counter +": "+ matcher.group(0));
            }
        }
    }
}
