package seedu.sourcecontrol.model.student.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));
    }

    @Test
    public void equals() {
        Tag tag1 = new Tag("beginner");

        //null -> returns false
        assertFalse(tag1.equals(null));

        //same Tag description -> returns true
        Tag tag2 = new Tag("beginner");
        assertTrue(tag1.equals(tag2));

        //same Tag object -> returns true
        assertTrue(tag1.equals(tag1));

        //different Tag description -> returns false;
        tag2 = new Tag("expert");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void getTagName() {
        Tag tag1 = new Tag("beginner");
        Tag tag2 = new Tag("expert");

        assertEquals(tag1.getTagName(), "beginner");
        assertEquals(tag2.getTagName(), "expert");
    }

    @Test
    public void toStringTest() {
        Tag tag1 = new Tag("beginner");
        Tag tag2 = new Tag("expert");

        assertEquals(tag1.toString(), "[beginner]");
        assertEquals(tag2.toString(), "[expert]");
    }
}
