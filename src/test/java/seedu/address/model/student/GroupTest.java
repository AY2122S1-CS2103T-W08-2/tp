package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class GroupTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Group(null));
    }

    @Test
    public void constructor_invalidGroupGroup_throwsIllegalArgumentException() {
        String invalidGroup = "";
        assertThrows(IllegalArgumentException.class, () -> new Group(invalidGroup));
    }

    @Test
    public void isValidGroup() {
        // null Group number
        assertThrows(NullPointerException.class, () -> Group.isValidGroup(null));

        // invalid Group numbers
        assertFalse(Group.isValidGroup("")); // empty string
        assertFalse(Group.isValidGroup(" ")); // spaces only

        // valid Group numbers
        assertTrue(Group.isValidGroup("T03C")); // upper case
        assertTrue(Group.isValidGroup("r05b")); // lower case
        assertTrue(Group.isValidGroup("r03A")); // mix of upper lower case
        assertTrue(Group.isValidGroup("r03 A")); // with spaces
        assertTrue(Group.isValidGroup("r")); // words only
        assertTrue(Group.isValidGroup("03")); // numbers only
    }

}
