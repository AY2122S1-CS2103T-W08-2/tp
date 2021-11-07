package seedu.sourcecontrol.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ALLOC_DESCRIPTOR_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ALLOC_DESCRIPTOR_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_TUTORIAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandFailureWithFilteredListChange;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.sourcecontrol.testutil.TypicalStudents.getTypicalSourceControl;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.logic.commands.AddAllocCommand.AllocDescriptor;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ModelManager;
import seedu.sourcecontrol.model.SourceControl;
import seedu.sourcecontrol.model.UserPrefs;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.testutil.AllocDescriptorBuilder;
import seedu.sourcecontrol.testutil.GroupBuilder;
import seedu.sourcecontrol.testutil.StudentBuilder;

/**
 * Contains integration tests (interaction with Model) and unit tests for AddAllocCommand.
 */
public class AddAllocCommandTest {

    private final Model model = new ModelManager(getTypicalSourceControl(), new UserPrefs());

    private final Student simpleAmy = new StudentBuilder()
            .withName(VALID_NAME_AMY)
            .withId(VALID_ID_AMY).build();

    private final Group simpleGroup = new GroupBuilder()
            .withName(VALID_GROUP_TUTORIAL).build();

    @Test
    public void execute_addAllocByName_success() {
        AllocDescriptor allocDescriptor = new AllocDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withGroup(VALID_GROUP_TUTORIAL).build();
        AddAllocCommand addAllocCommand = new AddAllocCommand(allocDescriptor);

        model.addStudent(simpleAmy);

        Model expectedModel = new ModelManager(new SourceControl(model.getSourceControl()), new UserPrefs());

        Group simpleGroupCopy = new Group(simpleGroup.name);
        model.addGroup(simpleGroup);
        expectedModel.addGroup(simpleGroupCopy);

        Student expectedAmy = new StudentBuilder(simpleAmy)
                .withGroups(VALID_GROUP_TUTORIAL).build();

        expectedModel.setStudent(simpleAmy, expectedAmy);

        String expectedMessage = String.format(AddAllocCommand.MESSAGE_SUCCESS, expectedAmy);

        assertCommandSuccess(addAllocCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addAllocById_success() {
        AllocDescriptor allocDescriptor = new AllocDescriptorBuilder()
                .withId(VALID_ID_AMY)
                .withGroup(VALID_GROUP_TUTORIAL).build();
        AddAllocCommand addAllocCommand = new AddAllocCommand(allocDescriptor);

        model.addStudent(simpleAmy);

        Model expectedModel = new ModelManager(new SourceControl(model.getSourceControl()), new UserPrefs());

        Group simpleGroupCopy = new Group(simpleGroup.name);
        model.addGroup(simpleGroup);
        expectedModel.addGroup(simpleGroupCopy);

        Student expectedAmy = new StudentBuilder(simpleAmy)
                .withGroups(VALID_GROUP_TUTORIAL).build();

        expectedModel.setStudent(simpleAmy, expectedAmy);

        String expectedMessage = String.format(AddAllocCommand.MESSAGE_SUCCESS, expectedAmy);

        assertCommandSuccess(addAllocCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nonexistentGroup_failure() {
        AllocDescriptor allocDescriptor = new AllocDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withGroup(VALID_GROUP_TUTORIAL).build();
        AddAllocCommand addAllocCommand = new AddAllocCommand(allocDescriptor);

        model.addStudent(simpleAmy);

        String expectedMessage = AddAllocCommand.MESSAGE_NONEXISTENT_GROUP;

        assertCommandFailure(addAllocCommand, model, expectedMessage);
    }

    @Test
    public void execute_nonexistentStudent_failure() {
        AllocDescriptor allocDescriptor = new AllocDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withGroup(VALID_GROUP_TUTORIAL).build();
        AddAllocCommand addAllocCommand = new AddAllocCommand(allocDescriptor);

        model.addGroup(simpleGroup);

        String expectedMessage = AddAllocCommand.MESSAGE_NONEXISTENT_STUDENT;

        assertCommandFailure(addAllocCommand, model, expectedMessage);
    }

    @Test
    public void execute_duplicateStudentName_failure() {
        AllocDescriptor allocDescriptor = new AllocDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withGroup(VALID_GROUP_TUTORIAL).build();
        AddAllocCommand addAllocCommand = new AddAllocCommand(allocDescriptor);

        Student duplicateAmy = new StudentBuilder()
                .withName(VALID_NAME_AMY)
                .withId(VALID_ID_BOB).build();

        model.addStudent(simpleAmy);
        model.addStudent(duplicateAmy);
        model.addGroup(simpleGroup);

        String expectedMessage = AddAllocCommand.MESSAGE_DUPLICATE_STUDENT_NAME;

        Model expectedModel = new ModelManager(new SourceControl(model.getSourceControl()), new UserPrefs());

        assertCommandFailureWithFilteredListChange(addAllocCommand, model, expectedMessage, expectedModel,
                VALID_NAME_AMY);
    }

    @Test
    public void execute_duplicateStudent_failure() {
        AllocDescriptor allocDescriptor = new AllocDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withGroup(VALID_GROUP_TUTORIAL).build();
        AddAllocCommand addAllocCommand = new AddAllocCommand(allocDescriptor);

        Group groupWithAmy = new GroupBuilder()
                .withName(VALID_GROUP_TUTORIAL)
                .withStudents(simpleAmy).build();

        model.addStudent(simpleAmy);
        model.addGroup(groupWithAmy);

        String expectedMessage = AddAllocCommand.MESSAGE_DUPLICATE_STUDENT;

        assertCommandFailure(addAllocCommand, model, expectedMessage);
    }

    @Test
    public void equal() {
        final AddAllocCommand standardCommand = new AddAllocCommand(ALLOC_DESCRIPTOR_AMY);

        // same values -> returns true
        AllocDescriptor copyDescriptor = new AllocDescriptor(ALLOC_DESCRIPTOR_AMY);
        AddAllocCommand commandWithSameValues = new AddAllocCommand(copyDescriptor);
        assertEquals(standardCommand, commandWithSameValues);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(standardCommand, null);

        // different types -> returns false
        assertNotEquals(standardCommand, new ClearCommand());

        // different descriptor -> returns false
        assertNotEquals(standardCommand, new AddAllocCommand(ALLOC_DESCRIPTOR_BOB));
    }
}
