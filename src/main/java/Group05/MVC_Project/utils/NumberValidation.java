package Group05.MVC_Project.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberValidation {
    public boolean validatePhone(String value) {
        // compiles the character sequence as a pattern
        Pattern pat = Pattern.compile("^\\d{8}$");
        // checks if there's a match with the pattern and the string value
        Matcher mat = pat.matcher(value);
        return mat.matches();
    }
}
