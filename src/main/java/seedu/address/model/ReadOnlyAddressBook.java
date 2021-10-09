package seedu.address.model;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.student.Assessment;
import seedu.address.model.student.Group;
import seedu.address.model.student.Student;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the students list.
     * This list will not contain any duplicate students.
     */
    ObservableList<Student> getStudentList();

    /**
     * Returns a list of groups.
     */
    List<Group> getGroupList();

    /**
     * Returns a list of assessments.
     */
    List<Assessment> getAssessmentList();
}
