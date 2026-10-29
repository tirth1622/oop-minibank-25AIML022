package minibank.util;

import java.util.regex.Pattern;

/**
 * Practical 4: Validation layer using pre-compiled regular expressions.
 * Ensures no invalid or malformed data enters the banking domain.
 */
public final class Validator {

    // Pre-compiled regex patterns for high-performance validation
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[6-9][0-9]{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PAN_PATTERN = Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]$");
    private static final Pattern IFSC_PATTERN = Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^[1-9][0-9]*$");

    private Validator() {
        // Prevent instantiation of utility class
    }

    /**
     * Matches standard 10-digit mobile numbers starting with 6, 7, 8, or 9.
     */
    public static boolean isValidMobile(String mobile) {
        return mobile != null && MOBILE_PATTERN.matcher(mobile.trim()).matches();
    }

    /**
     * Matches valid email address format with username and domain.
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Matches Indian PAN card format: 5 uppercase letters, 4 digits, 1 uppercase letter.
     */
    public static boolean isValidPan(String pan) {
        return pan != null && PAN_PATTERN.matcher(pan.trim()).matches();
    }

    /**
     * Matches Indian IFSC code format: 4 letters, 0, 6 alphanumeric characters.
     */
    public static boolean isValidIfsc(String ifsc) {
        return ifsc != null && IFSC_PATTERN.matcher(ifsc.trim()).matches();
    }

    /**
     * Practical 4 Supplementary: Validates positive whole numbers for monetary amounts.
     */
    public static boolean isValidAmount(String amountStr) {
        return amountStr != null && AMOUNT_PATTERN.matcher(amountStr.trim()).matches();
    }
}
