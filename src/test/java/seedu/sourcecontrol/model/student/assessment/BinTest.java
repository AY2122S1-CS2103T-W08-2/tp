package seedu.sourcecontrol.model.student.assessment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.model.student.assessment.AssessmentStatistics.Bin;

import org.junit.jupiter.api.Test;

public class BinTest {
    private static final Bin middleBin = new Bin(new Score("50"), new Score("60"));
    private static final Bin lastBin = new Bin(new Score("90"), new Score("100"));

    @Test
    public void includesScore_scoreInMiddleBin_returnsTrue() {
        assertTrue(middleBin.includesScore(new Score("50")));
        assertTrue(middleBin.includesScore(new Score("55")));
        assertTrue(middleBin.includesScore(new Score("59.99")));
    }

    @Test
    public void includesScore_scoreNotInMiddleBin_returnsFalse() {
        assertFalse(middleBin.includesScore(new Score("30")));
        assertFalse(middleBin.includesScore(new Score("60")));
        assertFalse(middleBin.includesScore(new Score("75")));
    }

    @Test
    public void includesScore_scoreInLastBin_returnsTrue() {
        assertTrue(lastBin.includesScore(new Score("90")));
        assertTrue(lastBin.includesScore(new Score("95")));
        assertTrue(lastBin.includesScore(new Score("100")));
    }

    @Test
    public void includesScore_scoreNotInLastBin_returnsFalse() {
        assertFalse(lastBin.includesScore(new Score("50")));
        assertFalse(lastBin.includesScore(new Score("89.99")));
    }

    @Test
    public void equals() {
        // same object -> returns true
        assertTrue(middleBin.equals(middleBin));

        // same min and max -> returns true
        assertTrue(middleBin.equals(new Bin(new Score("50.00"), new Score("60.00"))));

        // null -> returns false
        assertFalse(middleBin.equals(null));

        // different type -> returns false
        assertFalse(middleBin.equals(5));

        // different bin values -> returns false
        assertFalse(middleBin.equals(lastBin));

        // different minimum value -> returns false
        assertFalse(middleBin.equals(new Bin(new Score("20"), new Score("40"))));

        // different maximum value -> returns false
        assertFalse(middleBin.equals(new Bin(new Score("30"), new Score("50"))));

    }
}
