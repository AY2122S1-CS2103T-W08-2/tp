package seedu.sourcecontrol.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.EDIT_DESCRIPTOR_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.EDIT_DESCRIPTOR_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_RECITATION;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.sourcecontrol.testutil.EditStudentDescriptorBuilder;

public class EditStudentDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditStudentDescriptor descriptorWithSameValues = new EditStudentDescriptor(EDIT_DESCRIPTOR_AMY);
        assertEquals(EDIT_DESCRIPTOR_AMY, descriptorWithSameValues);

        // same object -> returns true
        assertEquals(EDIT_DESCRIPTOR_AMY, EDIT_DESCRIPTOR_AMY);

        // null -> returns false
        assertNotEquals(EDIT_DESCRIPTOR_AMY, null);

        // different types -> returns false
        assertNotEquals(EDIT_DESCRIPTOR_AMY, 5);

        // different values -> returns false
        assertNotEquals(EDIT_DESCRIPTOR_AMY, EDIT_DESCRIPTOR_BOB);

        // different name -> returns false
        EditStudentDescriptor editedAmy =
                new EditStudentDescriptorBuilder(EDIT_DESCRIPTOR_AMY).withName(VALID_NAME_BOB).build();
        assertNotEquals(EDIT_DESCRIPTOR_AMY, editedAmy);

        // different ID -> returns false
        editedAmy = new EditStudentDescriptorBuilder(EDIT_DESCRIPTOR_AMY).withId(VALID_ID_BOB).build();
        assertNotEquals(EDIT_DESCRIPTOR_AMY, editedAmy);

        // different groups -> returns false
        editedAmy = new EditStudentDescriptorBuilder(EDIT_DESCRIPTOR_AMY).withGroups(VALID_GROUP_RECITATION).build();
        assertNotEquals(EDIT_DESCRIPTOR_AMY, editedAmy);

        // different tags -> returns false
        editedAmy = new EditStudentDescriptorBuilder(EDIT_DESCRIPTOR_AMY).withTags(VALID_TAG_HUSBAND).build();
        assertNotEquals(EDIT_DESCRIPTOR_AMY, editedAmy);
    }
}
