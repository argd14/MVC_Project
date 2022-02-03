package Group05.MVC_Project.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberValidation {
    public boolean validatePhone(String value) {
        // compiles the character sequence as a pattern
        Pattern pat = Pattern.compile("/^[2,6,7]{1}[0-9]{3}[-][0-9]{4}$/");
        // checks if there's a match with the pattern and the string value
        Matcher mat = pat.matcher(value);
        return mat.matches();
    }
}
