package seedu.sourcecontrol.model.student.assessment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_SCORES_AMY;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;
import static seedu.sourcecontrol.testutil.TypicalAssessments.MISSION_01;
import static seedu.sourcecontrol.testutil.TypicalAssessments.PATH_05;
import static seedu.sourcecontrol.testutil.TypicalStudents.AMY;
import static seedu.sourcecontrol.testutil.TypicalStudents.BOB;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.exceptions.AssessmentNotFoundException;
import seedu.sourcecontrol.model.student.exceptions.DuplicateAssessmentException;
import seedu.sourcecontrol.testutil.AssessmentBuilder;
import seedu.sourcecontrol.testutil.AssessmentListBuilder;
import seedu.sourcecontrol.testutil.StudentBuilder;

public class AssessmentListTest {

    private final AssessmentList assessments = new AssessmentList();

    @Test
    public void contains_nullAssessment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> assessments.contains(null));
    }

    @Test
    public void contains_assessmentNotInList_returnsFalse() {
        assertFalse(assessments.contains(PATH_05));
    }

    @Test
    public void contains_assessmentInList_returnsTrue() {
        assessments.add(PATH_05);
        assertTrue(assessments.contains(PATH_05));
    }

    @Test
    public void contains_assessmentWithSameNameInList_returnsTrue() {
        assessments.add(PATH_05);
        Assessment sameNameAssessment = new AssessmentBuilder(PATH_05).build();
        assertTrue(assessments.contains(sameNameAssessment));
    }

    @Test
    public void add_nullAssessment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> assessments.add(null));
    }

    @Test
    public void add_duplicateAssessment_throwsDuplicateAssessmentException() {
        assessments.add(PATH_05);
        assertThrows(DuplicateAssessmentException.class, () -> assessments.add(PATH_05));
    }

    @Test
    public void add_validAssessment_success() {
        assessments.add(PATH_05);
        AssessmentList expectedAssessments = new AssessmentListBuilder()
                .withStudents(List.of(AMY)).build();
        assertEquals(expectedAssessments, assessments);
    }

    @Test
    public void update_nullStudentToUpdate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> assessments.update(null));
    }

    @Test
    public void update_studentToUpdateWithNewAssessment_successWithNewAssessmentAdded() {
        assessments.update(AMY);
        AssessmentList expectedAssessments = new AssessmentListBuilder()
                .withStudents(List.of(AMY)).build();
        assertEquals(expectedAssessments, assessments);
    }

    @Test
    public void update_studentToUpdateWithExistentAssessment_successWithExistentAssessmentUpdated() {
        Student updatedBob = new StudentBuilder(BOB).withScores(VALID_SCORES_AMY).build(); // Bob with Amy's scores
        assessments.update(AMY);
        assessments.update(updatedBob);
        AssessmentList expectedAssessments = new AssessmentListBuilder()
                .withStudents(List.of(AMY, updatedBob)).build();
        assertEquals(expectedAssessments, assessments);
    }

    @Test
    public void remove_nullAssessment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> assessments.remove(null));
    }

    @Test
    public void remove_assessmentDoesNotExist_throwsAssessmentNotFoundException() {
        assertThrows(AssessmentNotFoundException.class, () -> assessments.remove(PATH_05));
    }

    @Test
    public void remove_existingAssessment_removesAssessment() {
        assessments.add(PATH_05);
        assessments.remove(PATH_05);
        AssessmentList expectedUniqueAssessmentList = new AssessmentListBuilder().build();
        assertEquals(expectedUniqueAssessmentList, assessments);
    }

    @Test
    public void setAssessment_nullTargetAssessment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> assessments.setAssessment(null, PATH_05));
    }

    @Test
    public void setAssessment_nullEditedAssessment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> assessments.setAssessment(PATH_05, null));
    }

    @Test
    public void setAssessment_targetAssessmentNotInList_throwsAssessmentNotFoundException() {
        assertThrows(AssessmentNotFoundException.class, () -> assessments.setAssessment(PATH_05, PATH_05));
    }

    @Test
    public void setAssessment_editedAssessmentIsSameAssessment_success() {
        assessments.add(PATH_05);
        assessments.setAssessment(PATH_05, PATH_05);
        AssessmentList expectedAssessmentList = new AssessmentListBuilder()
                .withStudents(List.of(AMY)).build();
        assertEquals(expectedAssessmentList, assessments);
    }

    @Test
    public void setAssessment_editedAssessmentHasSameName_success() {
        Assessment sameNameAssessment = new AssessmentBuilder(PATH_05).build();
        assessments.add(PATH_05);
        assessments.setAssessment(PATH_05, sameNameAssessment);
        AssessmentList expectedAssessmentList = new AssessmentListBuilder()
                .withStudents(List.of(AMY)).build();
        assertEquals(expectedAssessmentList, assessments);
    }

    @Test
    public void setAssessment_editedAssessmentHasDifferentIdentity_success() {
        assessments.add(PATH_05);
        assessments.setAssessment(PATH_05, MISSION_01);
        AssessmentList expectedUniqueAssessmentList = new AssessmentListBuilder()
                .withStudents(List.of(BOB)).build();
        assertEquals(expectedUniqueAssessmentList, assessments);
    }

    @Test
    public void setAssessment_editedAssessmentHasNonUniqueIdentity_throwsDuplicateAssessmentException() {
        assessments.add(PATH_05);
        assessments.add(MISSION_01);
        assertThrows(DuplicateAssessmentException.class, () -> assessments.setAssessment(PATH_05, MISSION_01));
    }

    @Test
    public void setAssessments_nullUniqueAssessmentList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> assessments.setAssessments((AssessmentList) null));
    }

    @Test
    public void setAssessments_assessments_replacesOwnListWithProvidedUniqueAssessmentList() {
        AssessmentList expectedUniqueAssessmentList = new AssessmentListBuilder()
                .withStudents(List.of(BOB)).build();
        assessments.add(PATH_05);
        assessments.setAssessments(expectedUniqueAssessmentList);
        assertEquals(expectedUniqueAssessmentList, assessments);
    }

    @Test
    public void setAssessments_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> assessments.setAssessments((List<Assessment>) null));
    }

    @Test
    public void setAssessments_list_replacesOwnListWithProvidedList() {
        List<Assessment> providedList = List.of(MISSION_01);
        assessments.add(PATH_05);
        assessments.setAssessments(providedList);
        AssessmentList expectedAssessmentList = new AssessmentListBuilder()
                .withStudents(List.of(BOB)).build();
        assertEquals(expectedAssessmentList, assessments);
    }

    @Test
    public void setAssessments_listWithDuplicateAssessments_throwsDuplicateAssessmentException() {
        List<Assessment> listWithDuplicateAssessments = Arrays.asList(PATH_05, PATH_05);
        assertThrows(DuplicateAssessmentException.class, () ->
                assessments.setAssessments(listWithDuplicateAssessments));
    }
}
