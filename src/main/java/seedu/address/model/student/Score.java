package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Student's score for an assessment.
 * Guarantees: immutable; is valid as declared in {@link #isValidScore(String)}
 */
public class Score {

    public static final String MESSAGE_CONSTRAINTS =
            "Score should be in percentage format, and it should have at most 2 decimal places";
    public static final String INTEGRAL_PART_REGEX = "\\d{1,2}";
    public static final String DECIMAL_PART_REGEX = "(\\.\\d{1,2})?";
    public static final String MAX_SCORE_REGEX = "|100.00|100.0|100";
    public static final String VALIDATION_REGEX = INTEGRAL_PART_REGEX + DECIMAL_PART_REGEX + MAX_SCORE_REGEX;
    public final String value;

    /**
     * Constructs a {@code Score}.
     *
     * @param score A valid score.
     */
    public Score(String score) {
        requireNonNull(score);
        checkArgument(isValidScore(score), MESSAGE_CONSTRAINTS);
        value = reformatScore(score);
    }

    /**
     * Returns true if a given string is a valid score.
     */
    public static boolean isValidScore(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Reformats valid score to 2 decimal places.
     */
    public static String reformatScore(String score) {
        String[] parts = score.split("\\."); // split along decimal point
        assert parts.length == 2 || parts.length == 1; // integral part must exist and decimal part is optional
        String integral = parts[0];
        String decimal = parts.length == 2 ? parts[1] : "00";
        assert decimal.length() <= 2; // decimal part contains at most 2 digits
        decimal += "0".repeat(2 - decimal.length()); // add additional 0 as needed
        return integral + "." + decimal;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Score // instanceof handles nulls
                && value.equals(((Score) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
