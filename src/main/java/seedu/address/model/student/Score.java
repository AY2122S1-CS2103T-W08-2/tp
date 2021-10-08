package seedu.address.model.student;

import java.util.Objects;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Student's score for an assessment.
 * Guarantees: is valid as declared in {@link #isValidScore(String)}
 */
public class Score {

    public static final String MESSAGE_CONSTRAINTS =
            "Score should be in percentage format, and it should have at most 2 decimal places";
    public static final String INTEGRAL_PART_REGEX = "\\d{1,2}";
    public static final String DECIMAL_PART_REGEX = "(\\.\\d{1,2})?";
    public static final String MAX_SCORE_REGEX = "|100.00|100.0|100";
    public static final String VALIDATION_REGEX = INTEGRAL_PART_REGEX + DECIMAL_PART_REGEX + MAX_SCORE_REGEX;
    private String score;
    private final Student student;
    private final Assessment assessment;

    /**
     * Constructs a {@code Score}.
     */
    private Score(Assessment assessment, Student student, String score) {
        this.assessment = assessment;
        this.student = student;
        this.score = reformatScore(score);
    }

    /**
     * Factory method for creating a {@code Score} associated with {@code student} and {@code assessment}.
     */
    public static void updateScore(Assessment assessment, Student student, String score) {
        requireAllNonNull(assessment, student, score);
        checkArgument(isValidScore(score), MESSAGE_CONSTRAINTS);

        // if score exists, update the value
        for (Score assessmentScore : assessment.getScores()) {
            if (assessmentScore.getStudent().isSameStudent(student)) {
                assessmentScore.setScore(score);
                return;
            }
        }

        // score doesn't exist, so create a new score
        Score newScore = new Score(assessment, student, score);
        student.addScore(newScore);
        assessment.addScore(newScore);
    }

    private void setScore(String score) {
        this.score = reformatScore(score);
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public Student getStudent() {
        return student;
    }

    public String getScore() {
        return score;
    }

    public void deleteScore() {
        student.removeScore(this);
        assessment.removeScore(this);
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
        return score;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Score // instanceof handles nulls
                && score.equals(((Score) other).score) // state check
                && student.equals(((Score) other).student) // state check
                && assessment.equals(((Score) other).assessment)); // state check
    }

    @Override
    public int hashCode() {
        return Objects.hash(score, student, assessment);
    }

}
