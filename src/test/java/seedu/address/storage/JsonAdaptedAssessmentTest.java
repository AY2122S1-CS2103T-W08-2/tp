package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.student.Assessment;

public class JsonAdaptedAssessmentTest {
    // TODO: Make TypicalAssessments, then test with those
    private static final String VALID_ASSESSMENT = BENSON.getScores().keySet().iterator().next().value;
    private static final String INVALID_ASSESSMENT = " ";

    @Test
    public void toModelType_validAssessmentDetails_returnsGroup() throws Exception {
        JsonAdaptedAssessment assessment = new JsonAdaptedAssessment(VALID_ASSESSMENT, new HashMap<>());
        assertEquals(new Assessment(VALID_ASSESSMENT), assessment.toModelType());
    }

    @Test
    public void toModelType_invalidAssessmentName_throwsIllegalValueException() {
        JsonAdaptedAssessment assessment = new JsonAdaptedAssessment(INVALID_ASSESSMENT, new HashMap<>());
        String expectedMessage = Assessment.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, assessment::toModelType);
    }
}
