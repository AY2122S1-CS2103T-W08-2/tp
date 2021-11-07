package seedu.sourcecontrol.model.student.id;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.commons.util.AppUtil.checkArgument;

public class Id {

    public static final String MESSAGE_CONSTRAINTS =
            "ID should start with E followed by 7 numerical numbers";
    public static final String VALIDATION_REGEX = "[Ee]\\d{7}";
    public final String value;

    /**
     * Constructs a {@code Id}.
     *
     * @param id A valid NUSNET ID.
     */
    public Id(String id) {
        requireNonNull(id);
        checkArgument(isValidID(id), MESSAGE_CONSTRAINTS);
        value = reformatId(id);
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidID(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Reformats valid ID to uppercase.
     */
    public static String reformatId(String name) {
        assert name.length() == 8; // ID should already be validated
        char upperCase = Character.toUpperCase(name.charAt(0));
        return upperCase + name.substring(1);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Id // instanceof handles nulls
                && value.equals(((Id) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
