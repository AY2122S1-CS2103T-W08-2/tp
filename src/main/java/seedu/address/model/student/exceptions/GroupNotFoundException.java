package seedu.address.model.student.exceptions;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException() {
        super("Group does not exist");
    }
}
