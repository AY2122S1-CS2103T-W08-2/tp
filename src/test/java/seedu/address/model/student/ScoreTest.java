package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

public class ScoreTest {

    @Test
    public void updateScore_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Score.updateScore(null, null, null));
    }

    @Test
    public void updateScore_invalidScore_throwsIllegalArgumentException() {
        String invalidScore = "";
        assertThrows(IllegalArgumentException.class, () -> Score.updateScore(
                new Assessment("a"),
                new Student(new Name("a"), new ID("e1234567"), new HashSet<>()),
                invalidScore));
    }

    @Test
    public void isValidScore() {
        // null score
        assertThrows(NullPointerException.class, () -> Score.isValidScore(null));

        // invalid scores
        assertFalse(Score.isValidScore("")); // empty string
        assertFalse(Score.isValidScore(" ")); // spaces only
        assertFalse(Score.isValidScore("score")); // non-numeric
        assertFalse(Score.isValidScore("1101s")); // alphabets within digits
        assertFalse(Score.isValidScore("101")); // exceeding max score
        assertFalse(Score.isValidScore("100.1"));
        assertFalse(Score.isValidScore("100.01"));
        assertFalse(Score.isValidScore("11 .01")); // spaces within digits
        assertFalse(Score.isValidScore("1101")); // missing decimal point
        assertFalse(Score.isValidScore("11.")); // missing integral part
        assertFalse(Score.isValidScore(".01")); // missing decimal part
        assertFalse(Score.isValidScore("11.001")); // more than 2 digits

        // valid scores
        assertTrue(Score.isValidScore("100")); // max score
        assertTrue(Score.isValidScore("100.0"));
        assertTrue(Score.isValidScore("100.00"));
        assertTrue(Score.isValidScore("0")); // min score
        assertTrue(Score.isValidScore("0.0"));
        assertTrue(Score.isValidScore("0.00"));
        assertTrue(Score.isValidScore("11")); // integer score
        assertTrue(Score.isValidScore("11.01"));
    }

    @Test
    public void reformatScore() {
        assertEquals("11.01", Score.reformatScore("11.01")); // unchanged
        assertEquals("100.00", Score.reformatScore("100.00"));
        assertEquals("0.00", Score.reformatScore("0.00"));
        assertEquals("11.10", Score.reformatScore("11.1")); // adding 1 additional 0
        assertEquals("100.00", Score.reformatScore("100.0"));
        assertEquals("0.00", Score.reformatScore("0.0"));
        assertEquals("11.00", Score.reformatScore("11.0")); // adding 2 additional 0s
        assertEquals("100.00", Score.reformatScore("100"));
        assertEquals("0.00", Score.reformatScore("0"));
    }
}
