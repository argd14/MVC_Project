package Group05.MVC_Project.utils;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidation {
    public boolean validateAlphabetic(String value, int max) {
        if (value.length() <= max) {
            // compiles the character sequence as a pattern
            Pattern pat = Pattern.compile("[a-z A-Z]+");
            // checks if there's a match with the pattern and the string value
            Matcher mat = pat.matcher(value);
            return mat.matches();
        } else {
            return false;
        }
    }

    public boolean validateAlphanumeric(String value, int max) {
        if (value.length() <= max) {
            // compiles the character sequence as a pattern
            Pattern pat = Pattern.compile("^[a-zA-Z0-9_ .]+$");
            // checks if there's a match with the pattern and the value
            Matcher mat = pat.matcher(value);
            return mat.matches();
        } else {
            return false;
        }
    }

    public boolean validateEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
}