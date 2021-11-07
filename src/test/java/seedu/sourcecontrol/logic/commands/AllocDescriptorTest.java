package seedu.sourcecontrol.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ALLOC_DESCRIPTOR_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ALLOC_DESCRIPTOR_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_RECITATION;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_BOB;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.logic.commands.AddAllocCommand.AllocDescriptor;
import seedu.sourcecontrol.testutil.AllocDescriptorBuilder;

public class AllocDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        AllocDescriptor descriptorWithSameValues = new AllocDescriptor(ALLOC_DESCRIPTOR_AMY);
        assertEquals(ALLOC_DESCRIPTOR_AMY, descriptorWithSameValues);

        // same object -> returns true
        assertEquals(ALLOC_DESCRIPTOR_AMY, ALLOC_DESCRIPTOR_AMY);

        // null -> returns false
        assertNotEquals(ALLOC_DESCRIPTOR_AMY, null);

        // different types -> returns false
        assertNotEquals(ALLOC_DESCRIPTOR_AMY, 5);

        // different values -> returns false
        assertNotEquals(ALLOC_DESCRIPTOR_AMY, ALLOC_DESCRIPTOR_BOB);

        // different group -> returns false
        AllocDescriptor editedAmy = new AllocDescriptorBuilder(ALLOC_DESCRIPTOR_AMY)
                .withGroup(VALID_GROUP_RECITATION).build();
        assertNotEquals(ALLOC_DESCRIPTOR_AMY, editedAmy);

        // different name -> returns false
        editedAmy = new AllocDescriptorBuilder(ALLOC_DESCRIPTOR_AMY)
                .withName(VALID_NAME_BOB).build();
        assertNotEquals(ALLOC_DESCRIPTOR_AMY, editedAmy);

        // different ID -> returns false
        editedAmy = new AllocDescriptorBuilder(ALLOC_DESCRIPTOR_AMY)
                .withId(VALID_ID_BOB).build();
        assertNotEquals(ALLOC_DESCRIPTOR_AMY, editedAmy);
    }
}
