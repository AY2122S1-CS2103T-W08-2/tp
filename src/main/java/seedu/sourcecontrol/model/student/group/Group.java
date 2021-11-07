package seedu.sourcecontrol.model.student.group;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.commons.util.AppUtil.checkArgument;

import java.util.ArrayList;
import java.util.List;

import seedu.sourcecontrol.model.student.id.Id;

public class Group {

    public static final String MESSAGE_CONSTRAINTS =
            "Group names should only contain alphanumeric characters and spaces, and it should not be blank";
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    // Group student list
    public final List<Id> students;

    // Group name
    public final String name;

    /**
     * Constructs a {@code Group}.
     *
     * @param name A valid Group name.
     */
    public Group(String name) {
        requireNonNull(name);
        checkArgument(isValidGroup(name), MESSAGE_CONSTRAINTS);
        this.name = name;
        this.students = new ArrayList<>();
    }

    /**
     * Constructs a {@code Group} with the specified students.
     *
     * @param name A valid Group name.
     * @param students Students to add into the group.
     */
    public Group(String name, List<Id> students) {
        requireNonNull(name);
        checkArgument(isValidGroup(name), MESSAGE_CONSTRAINTS);
        this.name = name;
        this.students = new ArrayList<>(students);
    }

    /**
     * Returns true if the given string {@code test} is a valid group name.
     */
    public static boolean isValidGroup(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public String getName() {
        return name;
    }

    public List<Id> getStudents() {
        return students;
    }

    /**
     * Returns true if the given ID {@code id} specifies a student in the group.
     */
    public boolean hasStudent(Id id) {
        requireNonNull(id);
        return students.contains(id);
    }

    /**
     * Adds student with ID {@code id} to this {@code Group}.
     * {@code id} must not already exist in the group.
     */
    public void addStudent(Id id) {
        requireNonNull(id);
        if (!hasStudent(id)) {
            students.add(id);
        }
    }

    /**
     * Removes student with ID {@code key} from this {@code Group}.
     * {@code key} must exist in the group.
     */
    public void removeStudent(Id key) {
        students.remove(key);
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
