package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Student's assessment.
 * Guarantees: immutable; is valid as declared in {@link #isValidAssessment(String)}
 */
public class Assessment {

    public static final String MESSAGE_CONSTRAINTS =
            "Assessment names should only contain alphanumeric characters and spaces, and it should not be blank";
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    // Assessment score list
    public final Map<ID, Score> scores = new HashMap<>();

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
        value = name;
    }

    /**
     * Returns true if a given string is a valid assessment name.
     */
    public static boolean isValidAssessment(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public String getName() {
        return value;
    }

    public void setScores(ID id, Score score) {
        scores.put(id, score);
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns true if both assessments have the same name.
     * This defines a weaker notion of equality between two assessments.
     */
    public boolean isSameAssessment(Assessment otherAssessment) {
        if (otherAssessment == this) {
            return true;
        }

        return otherAssessment != null
                && otherAssessment.getName().equals(getName());
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
