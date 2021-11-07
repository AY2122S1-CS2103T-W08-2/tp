package seedu.sourcecontrol.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;
import static seedu.sourcecontrol.testutil.TypicalStudents.BENSON;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.commons.exceptions.IllegalValueException;
import seedu.sourcecontrol.model.student.group.Group;

public class JsonAdaptedGroupTest {
    private static final String VALID_GROUP = BENSON.getGroups().get(0).name;
    private static final String INVALID_GROUP = " ";

    @Test
    public void toModelType_validGroupDetails_returnsGroup() throws Exception {
        JsonAdaptedGroup group = new JsonAdaptedGroup(VALID_GROUP);
        assertEquals(BENSON.getGroups().get(0), group.toModelType());
    }

    @Test
    public void toModelType_invalidGroupName_throwsIllegalValueException() {
        JsonAdaptedGroup group = new JsonAdaptedGroup(INVALID_GROUP);
        String expectedMessage = Group.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, group::toModelType);
    }
}
