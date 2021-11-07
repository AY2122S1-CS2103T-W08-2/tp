package seedu.sourcecontrol.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;


public class AliasTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Alias(null, "commandWord"));
        assertThrows(NullPointerException.class, () -> new Alias("aliasWord", null));
        assertThrows(NullPointerException.class, () -> new Alias(null, null));
    }

    @Test
    public void constructor_invalidAlias_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Alias("", "commandWord"));
        assertThrows(IllegalArgumentException.class, () -> new Alias("alias word", "commandWord"));
    }

    @Test
    public void getAliasWord() {
        assertEquals(new Alias("aliasWord", "commandWord").getAliasWord(), "aliasWord");

        // case sensitive
        assertNotEquals(new Alias("AliasWord", "CommandWord").getAliasWord(), "aliasWord");
    }

    @Test
    public void getCommandWord() {
        assertEquals(new Alias("aliasWord", "commandWord").getCommandWord(), "commandWord");

        // case sensitive
        assertNotEquals(new Alias("AliasWord", "CommandWord").getCommandWord(), "commandWord");
    }

    @Test
    public void isRedundant() {
        assertTrue(new Alias("aliasWord", "aliasWord").isRedundant());

        // case sensitive
        assertFalse(new Alias("aliasWord", "AliasWord").isRedundant());
    }

    @Test
    public void replaceFirst() {
        Alias alias = new Alias("aliasWord", "commandWord");
        assertEquals(alias.replaceFirst("aliasWord hello world"), "commandWord hello world");
    }

    @Test
    public void equals() {
        assertEquals(new Alias("aliasWord", "commandWord"),
                new Alias("aliasWord", "commandWord"));

        assertNotEquals(new Alias("aliasWord", "commandWord"),
                new Alias("AliasWord", "commandWord"));

        assertNotEquals(new Alias("aliasWord", "commandWord"),
                new Alias("aliasWord", "CommandWord"));

        Alias alias = new Alias("aliasWord", "commandWord");
        assertEquals(alias, alias);
    }
}
