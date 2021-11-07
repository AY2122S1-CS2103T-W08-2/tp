package seedu.sourcecontrol.model.student.group;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.commons.util.CollectionUtil.equalsIgnoreOrder;
import static seedu.sourcecontrol.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import seedu.sourcecontrol.model.SourceControl;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.exceptions.DuplicateGroupException;
import seedu.sourcecontrol.model.student.exceptions.GroupNotFoundException;

public class GroupList {

    public static final String MESSAGE_CONSTRAINTS =
            "Number of Groups should be a positive integer";
    public static final String VALIDATION_REGEX = "\\d+";
    public final List<Group> groups = new ArrayList<>();

    /**
     * Returns true if a given string is a valid number of Groups.
     */
    public static boolean isValidNumOfGroups(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if the list contains an equivalent Group as the given argument.
     */
    public boolean contains(Group group) {
        requireNonNull(group);
        return groups.stream().anyMatch(group::equals);
    }

    /**
     * Adds a Group into the list.
     */
    public void add(Group group) {
        requireNonNull(group);
        if (contains(group)) {
            return;
        }
        groups.add(group);
    }

    /**
     * Updates the group list accordingly to the student info.
     * Ensures that the student is properly recorded in every group in the group list
     *
     * @see SourceControl#addStudent(Student)
     */
    public void update(Student toUpdate) {
        requireNonNull(toUpdate);

        List<Group> studentGroups = toUpdate.getGroups();
        for (Group group : studentGroups) {
            if (!groups.contains(group)) {
                groups.add(group);
            } else {
                Group groupInList = groups.get(groups.indexOf(group));
                groupInList.addStudent(toUpdate.getId());
            }
        }
    }

    /**
     * Replaces the Group {@code target} in the list with {@code editedGroup}.
     * {@code target} must exist in the list.
     * The Group identity of {@code editedGroup} must not be the same as another existing Group in the list.
     */
    public void setGroup(Group target, Group editedGroup) {
        requireAllNonNull(target, editedGroup);

        int index = groups.indexOf(target);
        if (index == -1) {
            throw new GroupNotFoundException();
        }

        if (!target.equals(editedGroup) && contains(editedGroup)) {
            throw new DuplicateGroupException();
        }

        groups.set(index, editedGroup);
    }

    /**
     * Removes any references to the student in groups.
     */
    public void removeStudent(Student toRemove) {
        requireNonNull(toRemove);

        for (Group group : groups) {
            if (group.hasStudent(toRemove.getId())) {
                group.removeStudent(toRemove.getId());
            }
        }
    }

    /**
     * Removes any references to the previous student and inserts references to the new student
     */
    public void replaceStudent(Student previous, Student edited) {
        requireAllNonNull(previous, edited);

        removeStudent(previous);
        update(edited);
    }

    /**
     * Removes the equivalent Group from the list.
     * The Group must exist in the list.
     */
    public void remove(Group group) {
        requireNonNull(group);
        if (!groups.remove(group)) {
            throw new GroupNotFoundException();
        }
    }

    /**
     * Replaces the contents of this list with {@code Group}.
     */
    public void setGroups(GroupList group) {
        requireNonNull(group);
        Collections.copy(groups, group.groups);
    }

    /**
     * Replaces the contents of this list with {@code groups}.
     * {@code groups} must not contain duplicate groups.
     */
    public void setGroups(List<Group> groups) {
        requireAllNonNull(groups);
        if (!groupsAreUnique(groups)) {
            throw new DuplicateGroupException();
        }

        this.groups.clear();
        this.groups.addAll(groups);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof GroupList // instanceof handles nulls
                && equalsIgnoreOrder(groups, ((GroupList) other).groups)); // state check
    }

    @Override
    public int hashCode() {
        return groups.hashCode();
    }

    /**
     * Returns true if {@code groups} contains only unique groups.
     */
    private boolean groupsAreUnique(List<Group> groups) {
        for (int i = 0; i < groups.size() - 1; i++) {
            for (int j = i + 1; j < groups.size(); j++) {
                if (groups.get(i).equals(groups.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

}
