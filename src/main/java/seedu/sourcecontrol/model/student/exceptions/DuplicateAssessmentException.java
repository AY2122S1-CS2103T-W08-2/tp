package seedu.sourcecontrol.model.student.exceptions;

/**
 * Signals that the operation will result in duplicate Assessments
 * (Assessments are considered duplicates if they have the same name).
 */
public class DuplicateAssessmentException extends RuntimeException {
    public DuplicateAssessmentException() {
        super("Operation would result in duplicate assessments");
    }
}
