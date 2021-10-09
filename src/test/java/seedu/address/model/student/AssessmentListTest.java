package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.student.exceptions.AssessmentNotFoundException;
import seedu.address.model.student.exceptions.DuplicateAssessmentException;

public class AssessmentListTest {

    private static final Assessment PATH_01 = new Assessment("P01");
    private static final Assessment MISSION_01 = new Assessment("M01");

    private final AssessmentList assessments = new AssessmentList();

    @Test
    public void contains_nullAssessment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> assessments.contains(null));
    }

    @Test
    public void contains_assessmentNotInList_returnsFalse() {
        assertFalse(assessments.contains(PATH_01));
    }

    @Test
    public void contains_assessmentInList_returnsTrue() {
        assessments.add(PATH_01);
        assertTrue(assessments.contains(PATH_01));
    }

    @Test
    public void contains_assessmentWithSameNameInList_returnsTrue() {
        assessments.add(PATH_01);
        assertTrue(assessments.contains(new Assessment(PATH_01.value)));
    }

    @Test
    public void add_nullAssessment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> assessments.add(null));
    }

    @Test
    public void add_duplicateAssessment_throwsDuplicateAssessmentException() {
        assessments.add(PATH_01);
        assertThrows(DuplicateAssessmentException.class, () -> assessments.add(PATH_01));
    }

    @Test
    public void setAssessment_nullTargetAssessment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> assessments.setAssessment(null, PATH_01));
    }

    @Test
    public void setAssessment_nullEditedAssessment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> assessments.setAssessment(PATH_01, null));
    }

    @Test
    public void setAssessment_targetAssessmentNotInList_throwsAssessmentNotFoundException() {
        assertThrows(AssessmentNotFoundException.class, () -> assessments.setAssessment(PATH_01, PATH_01));
    }

    @Test
    public void setAssessment_editedAssessmentIsSameAssessment_success() {
        assessments.add(PATH_01);
        assessments.setAssessment(PATH_01, PATH_01);
        AssessmentList expectedAssessmentList = new AssessmentList();
        expectedAssessmentList.add(PATH_01);
        assertEquals(expectedAssessmentList, assessments);
    }

    @Test
    public void setAssessment_editedAssessmentHasSameName_success() {
        assessments.add(PATH_01);
        Assessment editedPath01 = new Assessment(PATH_01.value);
        assessments.setAssessment(PATH_01, editedPath01);
        AssessmentList expectedAssessmentList = new AssessmentList();
        expectedAssessmentList.add(editedPath01);
        assertEquals(expectedAssessmentList, assessments);
    }

    @Test
    public void setAssessment_editedAssessmentHasDifferentIdentity_success() {
        assessments.add(PATH_01);
        assessments.setAssessment(PATH_01, MISSION_01);
        AssessmentList expectedUniqueAssessmentList = new AssessmentList();
        expectedUniqueAssessmentList.add(MISSION_01);
        assertEquals(expectedUniqueAssessmentList, assessments);
    }

    @Test
    public void setAssessment_editedAssessmentHasNonUniqueIdentity_throwsDuplicateAssessmentException() {
        assessments.add(PATH_01);
        assessments.add(MISSION_01);
        assertThrows(DuplicateAssessmentException.class, () -> assessments.setAssessment(PATH_01, MISSION_01));
    }

    @Test
    public void remove_nullAssessment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> assessments.remove(null));
    }

    @Test
    public void remove_personDoesNotExist_throwsAssessmentNotFoundException() {
        assertThrows(AssessmentNotFoundException.class, () -> assessments.remove(PATH_01));
    }

    @Test
    public void remove_existingAssessment_removesAssessment() {
        assessments.add(PATH_01);
        assessments.remove(PATH_01);
        AssessmentList expectedUniqueAssessmentList = new AssessmentList();
        assertEquals(expectedUniqueAssessmentList, assessments);
    }

    @Test
    public void setAssessments_nullUniqueAssessmentList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> assessments.setAssessments((AssessmentList) null));
    }

    @Test
    public void setAssessments_assessments_replacesOwnListWithProvidedUniqueAssessmentList() {
        assessments.add(PATH_01);
        AssessmentList expectedUniqueAssessmentList = new AssessmentList();
        expectedUniqueAssessmentList.add(MISSION_01);
        assessments.setAssessments(expectedUniqueAssessmentList);
        assertEquals(expectedUniqueAssessmentList, assessments);
    }

    @Test
    public void setAssessments_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> assessments.setAssessments((List<Assessment>) null));
    }

    @Test
    public void setAssessments_list_replacesOwnListWithProvidedList() {
        assessments.add(PATH_01);
        List<Assessment> providedList = new ArrayList<>();
        providedList.add(MISSION_01);
        assessments.setAssessments(providedList);
        AssessmentList expectedAssessmentList = new AssessmentList();
        expectedAssessmentList.add(MISSION_01);
        assertEquals(expectedAssessmentList, assessments);
    }

    @Test
    public void setAssessments_listWithDuplicateAssessments_throwsDuplicateAssessmentException() {
        List<Assessment> listWithDuplicateAssessments = Arrays.asList(PATH_01, PATH_01);
        assertThrows(DuplicateAssessmentException.class, () ->
                assessments.setAssessments(listWithDuplicateAssessments));
    }
}
