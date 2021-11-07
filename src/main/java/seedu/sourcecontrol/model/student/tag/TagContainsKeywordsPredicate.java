package seedu.sourcecontrol.model.student.tag;

import java.util.List;
import java.util.function.Predicate;

import seedu.sourcecontrol.model.student.Student;

/**
 * Tests that a {@code Student}'s {@code Tag} matches any of the keywords given.
 * Supports partial searching of tags.
 */
public class TagContainsKeywordsPredicate implements Predicate<Student> {
    private final List<String> keywords;

    public TagContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Student student) {
        return student.getTags().stream()
                .anyMatch(tag -> keywords.stream()
                        .anyMatch(keyword -> tag.tagName.toLowerCase().contains(keyword.toLowerCase())));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((TagContainsKeywordsPredicate) other).keywords)); // state check
    }

}
