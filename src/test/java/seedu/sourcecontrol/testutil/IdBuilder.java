package seedu.sourcecontrol.testutil;

import seedu.sourcecontrol.model.student.id.Id;

/**
 * A utility class to help with building Id objects.
 */
public class IdBuilder {

    public static final String DEFAULT_VALUE = "E0123456";

    private String value;

    /**
     * Creates a {@code IdBuilder} with the default details.
     */
    public IdBuilder() {
        value = DEFAULT_VALUE;
    }

    /**
     * Initializes the IdBuilder with the data of {@code idToCopy}.
     */
    public IdBuilder(Id idToCopy) {
        value = idToCopy.getValue();
    }

    /**
     * Sets the {@code value} of the {@code id} that we are building.
     */
    public IdBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public Id build() {
        return new Id(value);
    }
}
