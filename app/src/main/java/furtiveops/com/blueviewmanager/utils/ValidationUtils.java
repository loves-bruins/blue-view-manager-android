package furtiveops.com.blueviewmanager.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lorenrogers on 1/26/17.
 */

public class ValidationUtils {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9!#$%&amp;'*+/=?^_`{|}~-]+(?:\\." +
            "[a-zA-Z0-9!#$%&amp;'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?" +
            "\\.)+[a-zA-Z]{2,}$";

    public static boolean isEmailValid(final String emailValue) {
        return isValid(emailValue, 200, 5, EMAIL_PATTERN);
    }

    private static boolean isValid(final String value, final int maxLen, final int minLen, final String pattern) {
        boolean infoValid;

        if (minLen > 0 && (value == null || value.equals(""))) {
            infoValid = false;
        }
        else if (value.length() < minLen || value.length() > maxLen) {
            infoValid = false;
        }
        else {
            final Pattern matchMe = Pattern.compile(pattern);
            final Matcher matcher = matchMe.matcher(value);
            infoValid = matcher.matches();
        }
        return infoValid;
    }
}
