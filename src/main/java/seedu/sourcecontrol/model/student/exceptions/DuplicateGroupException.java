package seedu.sourcecontrol.model.student.exceptions;

public class DuplicateGroupException extends RuntimeException {
    public DuplicateGroupException() {
        super("Operation would result in duplicate groups");
    }
}
