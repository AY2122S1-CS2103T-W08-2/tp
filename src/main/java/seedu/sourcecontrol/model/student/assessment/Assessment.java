package seedu.sourcecontrol.model.student.assessment;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.commons.util.AppUtil.checkArgument;

import java.util.LinkedHashMap;
import java.util.Map;

import seedu.sourcecontrol.model.student.id.Id;

/**
 * Represents a Student's assessment.
 * Guarantees: immutable; is valid as declared in {@link #isValidAssessment(String)}
 */
public class Assessment {

    public static final String MESSAGE_CONSTRAINTS =
            "Assessment names should only contain alphanumeric characters and spaces, and it should not be blank";
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    // Assessment score list
    public final Map<Id, Score> scores = new LinkedHashMap<>();

    // Assessment name
    public final String name;

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

    /**
     * Returns true if a given string is a valid assessment name.
     */
    public static boolean isValidAssessment(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public String getName() {
        return name;
    }

    public Map<Id, Score> getScores() {
        return scores;
    }

    public void setScore(Id id, Score score) {
        scores.put(id, score);
    }

    public void setScores(Map<Id, Score> scores) {
        this.scores.putAll(scores);
    }

    /**
     * Returns true if the student with the given {@code id} is already graded.
     */
    public boolean isGraded(Id id) {
        requireNonNull(id);
        return scores.containsKey(id);
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
                && name.equals(((Assessment) other).name)); // state check
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }


}
