package seedu.sourcecontrol.model.student.id;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.testutil.StudentBuilder;

public class IdContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("E0123456");
        List<String> secondPredicateKeywordList = Arrays.asList("E0123456", "873");

        IdContainsKeywordsPredicate firstPredicate = new IdContainsKeywordsPredicate(firstPredicateKeywordList);
        IdContainsKeywordsPredicate secondPredicate = new IdContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        IdContainsKeywordsPredicate firstPredicateCopy = new IdContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different student -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_idContainsKeywords_returnsTrue() {
        // One keyword
        IdContainsKeywordsPredicate predicate = new IdContainsKeywordsPredicate(Collections.singletonList("E0123456"));
        assertTrue(predicate.test(new StudentBuilder().withId("E0123456").build()));

        // Multiple keywords
        predicate = new IdContainsKeywordsPredicate(Arrays.asList("E012", "345"));
        assertTrue(predicate.test(new StudentBuilder().withId("E0123456").build()));

        // Only one matching keyword
        predicate = new IdContainsKeywordsPredicate(Arrays.asList("E0123456", "E6666666"));
        assertTrue(predicate.test(new StudentBuilder().withId("E0123456").build()));

        // Mixed-case keywords
        predicate = new IdContainsKeywordsPredicate(Arrays.asList("e0123456"));
        assertTrue(predicate.test(new StudentBuilder().withId("E0123456").build()));
    }

    @Test
    public void test_idDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        IdContainsKeywordsPredicate predicate = new IdContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new StudentBuilder().withId("E0123456").build()));

        // Non-matching keyword
        predicate = new IdContainsKeywordsPredicate(Arrays.asList("E6666666"));
        assertFalse(predicate.test(new StudentBuilder().withId("E0123456").build()));

        // Keywords match tag, but does not match ID
        predicate = new IdContainsKeywordsPredicate(Arrays.asList("E0123456"));
        assertFalse(predicate.test(new StudentBuilder().withId("E6666666").withTags("E0123456").build()));
    }
}
