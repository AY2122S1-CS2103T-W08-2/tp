package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AssessmentTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Assessment(null));
    }

    @Test
    public void constructor_invalidAssessment_throwsIllegalArgumentException() {
        String invalidAssessment = "";
        assertThrows(IllegalArgumentException.class, () -> new Assessment(invalidAssessment));
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
}
