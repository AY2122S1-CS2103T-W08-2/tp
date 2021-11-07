package seedu.sourcecontrol.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.sourcecontrol.commons.exceptions.IllegalValueException;
import seedu.sourcecontrol.model.student.group.Group;

public class JsonAdaptedGroup {
    private final String name;

    /**
     * Constructs a {@code JsonAdaptedGroup} with the given {@code groupName}.
     */
    @JsonCreator
    public JsonAdaptedGroup(@JsonProperty("name") String name) {
        this.name = name;
    }

    /**
     * Converts a given {@code Group} into this class for Jackson use.
     */
    public JsonAdaptedGroup(Group source) {
        name = source.name;
    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code Group} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted group.
     */
    public Group toModelType() throws IllegalValueException {
        if (!Group.isValidGroup(name)) {
            throw new IllegalValueException(Group.MESSAGE_CONSTRAINTS);
        }
        return new Group(name);
    }
}
