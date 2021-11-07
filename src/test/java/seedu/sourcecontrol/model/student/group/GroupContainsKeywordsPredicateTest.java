package seedu.sourcecontrol.model.student.group;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.testutil.StudentBuilder;

public class GroupContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("T02A");
        List<String> secondPredicateKeywordList = Arrays.asList("T02A", "R05B");

        GroupContainsKeywordsPredicate firstPredicate = new GroupContainsKeywordsPredicate(firstPredicateKeywordList);
        GroupContainsKeywordsPredicate secondPredicate = new GroupContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        GroupContainsKeywordsPredicate firstPredicateCopy =
                new GroupContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different student -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_groupContainsKeywords_returnsTrue() {
        // One keyword
        GroupContainsKeywordsPredicate predicate =
                new GroupContainsKeywordsPredicate(Collections.singletonList("T02A"));
        assertTrue(predicate.test(new StudentBuilder().withGroups("T02A").build()));

        // Multiple keywords
        predicate = new GroupContainsKeywordsPredicate(Arrays.asList("T02", "02A"));
        assertTrue(predicate.test(new StudentBuilder().withGroups("T02A").build()));

        // Only one matching keyword
        predicate = new GroupContainsKeywordsPredicate(Arrays.asList("T02A", "R05B"));
        assertTrue(predicate.test(new StudentBuilder().withGroups("T02A").build()));

        // Mixed-case keywords
        predicate = new GroupContainsKeywordsPredicate(Arrays.asList("t02a"));
        assertTrue(predicate.test(new StudentBuilder().withGroups("T02A").build()));
    }

    @Test
    public void test_groupDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        GroupContainsKeywordsPredicate predicate = new GroupContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new StudentBuilder().withGroups("T02A").build()));

        // Non-matching keyword
        predicate = new GroupContainsKeywordsPredicate(Arrays.asList("T03B"));
        assertFalse(predicate.test(new StudentBuilder().withGroups("T02A").build()));

        // Keywords match tag, but does not match group
        predicate = new GroupContainsKeywordsPredicate(Arrays.asList("T02A"));
        assertFalse(predicate.test(new StudentBuilder().withGroups("R05B").withTags("T02A").build()));
    }
}
