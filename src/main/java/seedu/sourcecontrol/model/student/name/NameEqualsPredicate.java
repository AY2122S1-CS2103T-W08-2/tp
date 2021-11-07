package seedu.sourcecontrol.model.student.name;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.sourcecontrol.model.student.Student;

/**
 * Tests that a {@code Student}'s {@code Name} matches the given name exactly.
 */
public class NameEqualsPredicate implements Predicate<Student> {
    private final String nameToMatch;

    /**
     * Creates a {@code NameEqualsPredicate} with the given name to match.
     */
    public NameEqualsPredicate(String nameToMatch) {
        requireNonNull(nameToMatch);
        this.nameToMatch = nameToMatch;
    }

    @Override
    public boolean test(Student student) {
        return nameToMatch.equals(student.getName().fullName);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameEqualsPredicate // instanceof handles nulls
                && nameToMatch.equals(((NameEqualsPredicate) other).nameToMatch)); // state check
    }

}
