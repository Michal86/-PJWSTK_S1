package zadania.regex.dodatkowe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PLWordValidator {
    private static final String WORD_PATTERN = "^a?ą?b?c?ć?d?e?ę?f?g?h?i?j?k?l?ł?m?n?o?ó?p?r?s?ś?t?u?w?y?z?ź?ż?";
    private Pattern pattern;
    private Matcher matcher;

    public PLWordValidator(){
        this.pattern = Pattern.compile(WORD_PATTERN);
    }

    public boolean checkWord(String word) {
        word = word.toLowerCase();

        this.matcher = pattern.matcher(word);
        return matcher.matches();
    }
}
