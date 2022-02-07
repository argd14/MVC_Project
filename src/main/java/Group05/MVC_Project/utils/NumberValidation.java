package Group05.MVC_Project.utils;

import org.apache.commons.lang3.math.NumberUtils;

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
    public boolean validateInteger(String value){
        try {
            @SuppressWarnings("unused")
            int x = Integer.parseInt(value);
            return true; //String is an Integer
        } catch (NumberFormatException e) {
            return false; //String is not an Integer
        }
    }
}
