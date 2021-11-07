package seedu.sourcecontrol.model.student.group;

import java.util.List;
import java.util.function.Predicate;

import seedu.sourcecontrol.model.student.Student;

/**
 * Tests that a {@code Student}'s {@code Group} matches any of the keywords given.
 * Supports partial searching of group names.
 */
public class GroupContainsKeywordsPredicate implements Predicate<Student> {
    private final List<String> keywords;

    /**
     * Creates a {@code GroupContainsKeywordsPredicate} with the given list of keywords.
     */
    public GroupContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Student student) {
        return student.getGroups().stream()
                .anyMatch(group -> keywords.stream()
                        .anyMatch(keyword -> group.name.toLowerCase().contains(keyword.toLowerCase())));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof GroupContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((GroupContainsKeywordsPredicate) other).keywords)); // state check
    }

}
