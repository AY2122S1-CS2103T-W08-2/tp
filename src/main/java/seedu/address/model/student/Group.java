package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class Group {
    public static final String MESSAGE_CONSTRAINTS =
            "Group name should not be blank";
    public static final String VALIDATION_REGEX = ".*\\S.*";

    // Group name
    private final String name;

    /**
     * Constructs an {@code Group}.
     *
     * @param name A valid Group name.
     */
    public Group(String name) {
        requireNonNull(name);
        checkArgument(isValidGroup(name), MESSAGE_CONSTRAINTS);
        this.name = name;
    }

    public Group(String name, Student... students) {
        requireNonNull(name);
        checkArgument(isValidGroup(name), MESSAGE_CONSTRAINTS);
        this.name = name;
        for (Student student : students) {
            addStudent(student);
        }
    }

    public void addStudent(Student student) {
        student.addGroup(this);
    }

    public String getName() {
        return name;
    }

    /**
     * Returns true if a given string is a valid Group name.
     */
    public static boolean isValidGroup(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Group // instanceof handles nulls
                && name.equals(((Group) other).name)); // state check
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
