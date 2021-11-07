package seedu.sourcecontrol.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;
import static seedu.sourcecontrol.testutil.TypicalStudents.BENSON;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.commons.exceptions.IllegalValueException;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.Score;
import seedu.sourcecontrol.model.student.id.Id;

public class JsonAdaptedAssessmentTest {
    private static final String VALID_ASSESSMENT = BENSON.getScores().keySet().iterator().next().name;
    private static final String INVALID_ASSESSMENT = " ";
    private static final String VALID_SCORE = BENSON.getScores().values().iterator().next().value;
    private static final String VALID_ID = BENSON.getId().value;
    private static final String INVALID_SCORE = "101";
    private static final String INVALID_ID = "A1234567";

    @Test
    public void toModelType_validAssessmentDetails_returnsGroup() throws Exception {
        JsonAdaptedAssessment assessment = new JsonAdaptedAssessment(VALID_ASSESSMENT, new LinkedHashMap<>());
        assertEquals(new Assessment(VALID_ASSESSMENT), assessment.toModelType());
    }

    @Test
    public void toModelType_invalidAssessmentName_throwsIllegalValueException() {
        JsonAdaptedAssessment assessment = new JsonAdaptedAssessment(INVALID_ASSESSMENT, new LinkedHashMap<>());
        String expectedMessage = Assessment.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, assessment::toModelType);
    }

    @Test
    public void toModelType_invalidId_throwsIllegalValueException() {
        LinkedHashMap<String, String> scores = new LinkedHashMap<>();
        scores.put(INVALID_ID, VALID_SCORE);
        JsonAdaptedAssessment assessment = new JsonAdaptedAssessment(VALID_ASSESSMENT, scores);
        String expectedMessage = Id.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, assessment::toModelType);
    }

    @Test
    public void toModelType_invalidScore_throwsIllegalValueException() {
        LinkedHashMap<String, String> scores = new LinkedHashMap<>();
        scores.put(VALID_ID, INVALID_SCORE);
        JsonAdaptedAssessment assessment = new JsonAdaptedAssessment(VALID_ASSESSMENT, scores);
        String expectedMessage = Score.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, assessment::toModelType);
    }

    @Test
    public void toModelType_nullScores_returnsNewAssessment() throws Exception {
        JsonAdaptedAssessment assessment = new JsonAdaptedAssessment(VALID_ASSESSMENT, null);
        assertEquals(assessment.toModelType(), new Assessment(VALID_ASSESSMENT));
    }
}
