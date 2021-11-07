package seedu.sourcecontrol.model.student.id;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class IdTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Id(null));
    }

    @Test
    public void constructor_invalidID_throwsIllegalArgumentException() {
        String invalidID = "";
        assertThrows(IllegalArgumentException.class, () -> new Id(invalidID));
    }

    @Test
    public void getValue() {
        assertEquals(new Id("E1234567").getValue(), "E1234567");

        // correctly format to capital letter
        assertEquals(new Id("e1234567").getValue(), "E1234567");
    }

    @Test
    public void isValidID() {
        // null ID number
        assertThrows(NullPointerException.class, () -> Id.isValidID(null));

        // invalid ID numbers
        assertFalse(Id.isValidID("")); // empty string
        assertFalse(Id.isValidID(" ")); // spaces only
        assertFalse(Id.isValidID("1231231")); // missing starting character
        assertFalse(Id.isValidID("A123 456")); // wrong starting character
        assertFalse(Id.isValidID("E12345678")); // exceed length
        assertFalse(Id.isValidID("E123456")); // below required length
        assertFalse(Id.isValidID("E123m231")); // alphabets within digits
        assertFalse(Id.isValidID("E123 456")); // spaces within digits

        // valid ID numbers
        assertTrue(Id.isValidID("E1234567")); // upper case
        assertTrue(Id.isValidID("e7654321")); // lower case
    }

}
