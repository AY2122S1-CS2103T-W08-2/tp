package seedu.sourcecontrol.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;
import static seedu.sourcecontrol.testutil.TypicalStudents.ALICE;
import static seedu.sourcecontrol.testutil.TypicalStudents.getTypicalSourceControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.exceptions.DuplicateStudentException;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.testutil.StudentBuilder;

public class SourceControlTest {

    private final SourceControl sourceControl = new SourceControl();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), sourceControl.getStudentList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> sourceControl.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlySourceControl_replacesData() {
        SourceControl newData = getTypicalSourceControl();
        sourceControl.resetData(newData);
        assertEquals(newData, sourceControl);
    }

    @Test
    public void resetData_withDuplicateStudents_throwsDuplicateStudentException() {
        // Two students with the same identity fields
        Student editedAlice = new StudentBuilder(ALICE).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Student> newStudents = Arrays.asList(ALICE, editedAlice);
        SourceControlStub newData = new SourceControlStub(newStudents);

        assertThrows(DuplicateStudentException.class, () -> sourceControl.resetData(newData));
    }

    @Test
    public void hasStudent_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> sourceControl.hasStudent(null));
    }

    @Test
    public void hasStudent_studentNotInSourceControl_returnsFalse() {
        assertFalse(sourceControl.hasStudent(ALICE));
    }

    @Test
    public void hasStudent_studentInSourceControl_returnsTrue() {
        sourceControl.addStudent(ALICE);
        assertTrue(sourceControl.hasStudent(ALICE));
    }

    @Test
    public void hasStudent_studentWithSameIdentityFieldsInSourceControl_returnsTrue() {
        sourceControl.addStudent(ALICE);
        Student editedAlice = new StudentBuilder(ALICE).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(sourceControl.hasStudent(editedAlice));
    }

    @Test
    public void getStudentList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> sourceControl.getStudentList().remove(0));
    }

    @Test
    public void getGroup_groupExists_success() {
        SourceControl sourceControl = new SourceControl();
        Group added = new Group("T01A");
        sourceControl.addGroup(added);
        assertSame(sourceControl.getGroup(new Group("T01A")), added);
    }

    @Test
    public void getAssessment_assessmentExists_success() {
        SourceControl sourceControl = new SourceControl();
        Assessment added = new Assessment("P01");
        sourceControl.addAssessment(added);
        assertSame(sourceControl.getAssessment(new Assessment("P01")), added);
    }

    @Test
    public void getGroup_groupDoesNotExist_returnNull() {
        SourceControl sourceControl = new SourceControl();
        Group added = new Group("T01A");
        sourceControl.addGroup(added);
        assertSame(sourceControl.getGroup(new Group("T01B")), null);
    }

    @Test
    public void getAssessment_assessmentDoesNotExist_returnNull() {
        SourceControl sourceControl = new SourceControl();
        Assessment added = new Assessment("P01");
        sourceControl.addAssessment(added);
        assertSame(sourceControl.getAssessment(new Assessment("M01")), null);
    }

    @Test
    public void equals_hashCode() {
        assertEquals(new SourceControl(), new SourceControl());
        assertEquals(new SourceControl().hashCode(), new SourceControl().hashCode());

        SourceControl sourceControl = new SourceControl();
        sourceControl.addAssessment(new Assessment("P01"));
        SourceControl sourceControl1 = new SourceControl(sourceControl);
        assertEquals(sourceControl, sourceControl1);
        assertEquals(sourceControl.hashCode(), sourceControl1.hashCode());

        sourceControl = new SourceControl();
        sourceControl.addStudent(new StudentBuilder(ALICE).build());
        sourceControl1 = new SourceControl(sourceControl);
        assertEquals(sourceControl, sourceControl1);
        assertEquals(sourceControl.hashCode(), sourceControl1.hashCode());

        sourceControl = new SourceControl();
        sourceControl.addGroup(new Group("T01A"));
        sourceControl1 = new SourceControl(sourceControl);
        assertEquals(sourceControl, sourceControl1);
        assertEquals(sourceControl.hashCode(), sourceControl1.hashCode());
    }

    /**
     * A stub ReadOnlySourceControl whose students list can violate interface constraints.
     */
    private static class SourceControlStub implements ReadOnlySourceControl {
        private final ObservableList<Student> students = FXCollections.observableArrayList();

        SourceControlStub(Collection<Student> students) {
            this.students.setAll(students);
        }

        @Override
        public ObservableList<Student> getStudentList() {
            return students;
        }

        @Override
        public List<Group> getGroupList() {
            return new ArrayList<>();
        }

        @Override
        public List<Assessment> getAssessmentList() {
            return new ArrayList<>();
        }
    }

}
