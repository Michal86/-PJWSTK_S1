package zadania.regex.dodatkowe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    private static final String PASSWORD_PATTERN = "^[0-9]{3}[\\w]{4}[*|^|%|#|~|!|&|\\||@|$]$";
    private Pattern pattern;
    private Matcher matcher;

    public PasswordValidator(){
        this.pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    // Validate given password
    public boolean validate(final String password){
        this.matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
