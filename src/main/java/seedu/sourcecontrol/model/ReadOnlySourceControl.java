package seedu.sourcecontrol.model;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.group.Group;

/**
 * Unmodifiable view of a Source Control.
 */
public interface ReadOnlySourceControl {

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
