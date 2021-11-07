package seedu.sourcecontrol.model.student.name;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.sourcecontrol.testutil.TypicalStudents.AMY;
import static seedu.sourcecontrol.testutil.TypicalStudents.BOB;

import org.junit.jupiter.api.Test;

public class NameEqualsPredicateTest {

    @Test
    public void equals() {
        String nameToMatch1 = VALID_NAME_AMY;
        String nameToMatch2 = VALID_NAME_BOB;

        NameEqualsPredicate predicate1 = new NameEqualsPredicate(nameToMatch1);
        NameEqualsPredicate predicate2 = new NameEqualsPredicate(nameToMatch2);

        // same object -> returns true
        assertTrue(predicate1.equals(predicate1));

        // same values -> returns true
        NameEqualsPredicate copy = new NameEqualsPredicate(nameToMatch1);
        assertTrue(predicate1.equals(copy));

        // different types -> returns false
        assertFalse(predicate1.equals(1));

        // null -> returns false
        assertFalse(predicate1.equals(null));

        // different values -> returns false
        assertFalse(predicate1.equals(predicate2));
    }

    @Test
    public void test_nameMatches_returnsTrue() {
        String nameToMatch1 = VALID_NAME_AMY;
        NameEqualsPredicate predicate1 = new NameEqualsPredicate(nameToMatch1);

        assertTrue(predicate1.test(AMY));
    }

    @Test
    public void test_nameDoesNotMatch_returnsFalse() {
        String nameToMatch = VALID_NAME_AMY;
        NameEqualsPredicate predicate = new NameEqualsPredicate(nameToMatch);

        //entirely different name -> returns false
        assertFalse(predicate.test(BOB));

        //partial match -> returns false
        predicate = new NameEqualsPredicate("Amy");
        assertFalse(predicate.test(AMY));
    }
}
