package seedu.sourcecontrol.model.student.assessment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_SCORE_AMY;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;
import static seedu.sourcecontrol.testutil.TypicalAssessments.PATH_05;

import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.testutil.AssessmentBuilder;
import seedu.sourcecontrol.testutil.IdBuilder;

public class AssessmentTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Assessment(null));
    }

    @Test
    public void constructor_invalidAssessment_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Assessment("%/(]"));
    }

    @Test
    public void isValidAssessment() {
        // null assessment
        assertThrows(NullPointerException.class, () -> Assessment.isValidAssessment(null));

        // invalid assessments
        assertFalse(Assessment.isValidAssessment("")); // empty string
        assertFalse(Assessment.isValidAssessment(" ")); // spaces only

        // valid assessments
        assertTrue(Assessment.isValidAssessment("P01")); // uppercase type
        assertTrue(Assessment.isValidAssessment("p01")); // lowercase type
        assertTrue(Assessment.isValidAssessment("p 01")); // with spaces
        assertTrue(Assessment.isValidAssessment("p")); // words only
        assertTrue(Assessment.isValidAssessment("01")); // numbers only
    }

    @Test
    public void isGraded_null_throwsNullPointerException() {
        Assessment assessment = new AssessmentBuilder().build();
        assertThrows(NullPointerException.class, () -> assessment.isGraded(null));
    }

    @Test
    public void isGraded_ungraded_returnsFalse() {
        Id id = new IdBuilder().build();
        Assessment assessment = new AssessmentBuilder().build();
        assertFalse(() -> assessment.isGraded(id));
    }

    @Test
    public void isGraded_graded_returnsTrue() {
        Id id = new IdBuilder().withValue(VALID_ID_AMY).build();
        Assessment assessment = new AssessmentBuilder()
                .withScores(Map.of(VALID_ID_AMY, VALID_SCORE_AMY)).build();
        assertTrue(() -> assessment.isGraded(id));
    }

    @Test
    public void isSameAssessment() {
        // null -> returns false
        assertFalse(PATH_05.isSameAssessment(null));

        // same object -> returns true
        assertTrue(PATH_05.isSameAssessment(PATH_05));

        // same name, different score list -> returns true
        Assessment editedPath1 = new AssessmentBuilder(PATH_05)
                .withScores(Map.of(VALID_ID_AMY, VALID_SCORE_AMY)).build();
        assertTrue(PATH_05.isSameAssessment(editedPath1));

        // different name, same score list -> returns false
        editedPath1 = new AssessmentBuilder(PATH_05).withValue("P02").build();
        assertFalse(PATH_05.isSameAssessment(editedPath1));

        // name differs in case, same score list -> returns false
        editedPath1 = new AssessmentBuilder(PATH_05).withValue("p01").build();
        assertFalse(PATH_05.isSameAssessment(editedPath1));

        // name has trailing spaces, same score list -> returns false
        editedPath1 = new AssessmentBuilder(PATH_05).withValue("P01 ").build();
        assertFalse(PATH_05.isSameAssessment(editedPath1));
    }
}
