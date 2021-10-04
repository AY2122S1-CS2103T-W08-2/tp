package seedu.address.model.person;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Student's assessment.
 * Guarantees: immutable; is valid as declared in {@link #isValidAssessment(String)}
 */
public class Assessment {

    public static final String MESSAGE_CONSTRAINTS =
            "Assessment name should be in AXX format, where A abbreviates the type and XX denotes the numbering";
    public static final String VALIDATION_REGEX = "[A-Za-z]\\d{2}";

    // Assessment score list
    public final Map<String, Score> scores = new HashMap<>();

    // Assessment name
    public final String value;

    /**
     * Constructs an {@code Assessment}.
     *
     * @param name A valid assessment name.
     */
    public Assessment(String name) {
        requireNonNull(name);
        checkArgument(isValidAssessment(name), MESSAGE_CONSTRAINTS);
        value = reformatAssessment(name);
    }

    /**
     * Returns true if a given string is a valid assessment name.
     */
    public static boolean isValidAssessment(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Reformats valid assessment name to uppercase.
     */
    public static String reformatAssessment(String name) {
        assert name.length() == 3; // assessment name should already be validated
        char type = Character.toUpperCase(name.charAt(0));
        return type + name.substring(1);
    }

    public void setScores(String id, Score score) {
        scores.put(id, score);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Assessment // instanceof handles nulls
                && value.equals(((Assessment) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
