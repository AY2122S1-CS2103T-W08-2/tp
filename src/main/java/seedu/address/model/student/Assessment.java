package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Student's assessment.
 * Guarantees: immutable; is valid as declared in {@link #isValidAssessment(String)}
 */
public class Assessment {
    public static final String MESSAGE_CONSTRAINTS =
            "Assessment name should not be blank";
    public static final String VALIDATION_REGEX = ".*\\S.*";

    // Assessment score list
    private final List<Score> scores = new ArrayList<>();

    // Assessment name
    private final String name;

    /**
     * Constructs an {@code Assessment}.
     *
     * @param name A valid assessment name.
     */
    public Assessment(String name) {
        requireNonNull(name);
        checkArgument(isValidAssessment(name), MESSAGE_CONSTRAINTS);
        this.name = name;
    }

    public void addScore(Score score) {
        if (scores.contains(score)) {
            return;
        }
        scores.add(score);
    }

    /**
     * Returns true if a given string is a valid assessment name.
     */
    public static boolean isValidAssessment(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public String getName() {
        return name;
    }

    public List<Score> getScores() {
        return Collections.unmodifiableList(scores);
    }

    public void removeScore(Score score) {
        scores.remove(score);
    }

    @Override
    public String toString() {
        return name;
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
                && name.equals(((Assessment) other).name) // state check
                && scores.equals(((Assessment) other).scores)); // state check
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
