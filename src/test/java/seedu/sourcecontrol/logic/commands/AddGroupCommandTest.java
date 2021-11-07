package seedu.sourcecontrol.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_RECITATION;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_TUTORIAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;
import static seedu.sourcecontrol.testutil.TypicalStudents.AMY;
import static seedu.sourcecontrol.testutil.TypicalStudents.BOB;
import static seedu.sourcecontrol.testutil.TypicalStudents.getTypicalSourceControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.logic.commands.AddAllocCommand.AllocDescriptor;
import seedu.sourcecontrol.logic.commands.exceptions.CommandException;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ModelManager;
import seedu.sourcecontrol.model.SourceControl;
import seedu.sourcecontrol.model.UserPrefs;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.testutil.AllocDescriptorBuilder;
import seedu.sourcecontrol.testutil.GroupBuilder;
import seedu.sourcecontrol.testutil.StudentBuilder;

public class AddGroupCommandTest {
    private static final String NEW_GROUP = "X01X";

    private Model model = new ModelManager(getTypicalSourceControl(), new UserPrefs());

    @Test
    public void constructor_nullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddGroupCommand(null, new ArrayList<>()));
    }

    @Test
    public void constructor_nullDescriptorList_throwsNullPointerException() {
        Group validGroup = new GroupBuilder().build();
        assertThrows(NullPointerException.class, () -> new AddGroupCommand(validGroup, null));
    }

    @Test
    public void execute_validGroupWithoutStudents_addSuccessful() throws Exception {
        Group groupToAdd = new GroupBuilder().withName(NEW_GROUP).build();
        AddGroupCommand command = new AddGroupCommand(groupToAdd, new ArrayList<>());

        Model expectedModel = new ModelManager(new SourceControl(model.getSourceControl()), new UserPrefs());
        expectedModel.addGroup(groupToAdd);

        assertCommandSuccess(command, model,
                String.format(AddGroupCommand.MESSAGE_SUCCESS, NEW_GROUP), expectedModel);
    }

    @Test
    public void execute_validGroupWithStudent_addSuccessful() throws Exception {
        // Student should already exist in the application
        Model expectedModel = new ModelManager(new SourceControl(model.getSourceControl()), new UserPrefs());
        model.addStudent(AMY);
        expectedModel.addStudent(AMY);

        Group groupToAdd = new GroupBuilder().withName(NEW_GROUP).build();
        AllocDescriptor amyNameDescriptor = new AllocDescriptorBuilder()
                .withGroup(NEW_GROUP)
                .withName(VALID_NAME_AMY).build();
        AddGroupCommand command = new AddGroupCommand(groupToAdd, Arrays.asList(amyNameDescriptor));

        // Expected model should have the new group with the student allocated to it
        Group completeGroup = new GroupBuilder().withName(NEW_GROUP).withStudents(AMY).build();
        expectedModel.addGroup(completeGroup);

        // Expected model's student should have the new group
        List<Group> newGroups = new ArrayList<>(AMY.getGroups());
        newGroups.add(completeGroup);
        expectedModel.setStudent(AMY,
                new Student(AMY.getName(), AMY.getId(), newGroups, AMY.getScores(), AMY.getTags()));

        assertCommandSuccess(command, model,
                command.formatSuccessMessage(Arrays.asList(AMY)), expectedModel);
    }

    @Test
    public void execute_duplicateGroup_throwsCommandException() {
        Group existingGroup;
        if (model.getSourceControl().getGroupList().size() > 0) {
            existingGroup = model.getSourceControl().getGroupList().get(0);
        } else {
            existingGroup = new GroupBuilder().build();
            model.addGroup(existingGroup);
        }

        AddGroupCommand command = new AddGroupCommand(existingGroup, new ArrayList<>());
        assertThrows(CommandException.class, AddGroupCommand.MESSAGE_DUPLICATE_GROUP, () -> command.execute(model));
    }

    @Test
    public void execute_nonExistentStudentName_throwsCommandException() {
        Group groupToAdd = new GroupBuilder().withName(NEW_GROUP).build();
        String randomName = "ZXC987";
        AllocDescriptor descriptorForNonExistentStudent = new AllocDescriptorBuilder()
                .withGroup(NEW_GROUP)
                .withName(randomName).build();
        AddGroupCommand command = new AddGroupCommand(groupToAdd, Arrays.asList(descriptorForNonExistentStudent));

        assertThrows(CommandException.class,
                String.format(AddGroupCommand.MESSAGE_NONEXISTENT_STUDENT, randomName), () -> command.execute(model));
    }

    @Test
    public void execute_nonExistentId_throwsCommandException() {
        Group groupToAdd = new GroupBuilder().withName(NEW_GROUP).build();
        String randomId = "E9090909";
        AllocDescriptor descriptorForNonExistentStudent = new AllocDescriptorBuilder()
                .withGroup(NEW_GROUP)
                .withId(randomId).build();

        AddGroupCommand command = new AddGroupCommand(groupToAdd, Arrays.asList(descriptorForNonExistentStudent));

        assertThrows(CommandException.class,
                String.format(AddGroupCommand.MESSAGE_NONEXISTENT_STUDENT, randomId), () -> command.execute(model));
    }

    @Test
    public void execute_allocatingSameStudentTwice_throwsCommandException() {
        // Student should already exist in the application
        model.addStudent(AMY);

        Group groupToAdd = new GroupBuilder().withName(NEW_GROUP).build();
        AllocDescriptor amyNameDescriptor = new AllocDescriptorBuilder()
                .withGroup(NEW_GROUP)
                .withName(VALID_NAME_AMY).build();
        AllocDescriptor amyIdDescriptor = new AllocDescriptorBuilder()
                .withGroup(NEW_GROUP)
                .withId(VALID_ID_AMY).build();

        AddGroupCommand command = new AddGroupCommand(groupToAdd, Arrays.asList(amyNameDescriptor, amyIdDescriptor));

        assertThrows(CommandException.class, String.format(AddGroupCommand.MESSAGE_DUPLICATE_STUDENT_IN_GROUP,
                AMY.getName(), AMY.getId()), () -> command.execute(model));
    }

    @Test
    public void execute_ambiguousStudentName_throwsCommandException() {
        // Two students named John Doe (with different IDs) exist in the application
        final String duplicatedName = "John Doe";
        Student firstJohn = new StudentBuilder().withName(duplicatedName).withId("E9090909").build();
        Student secondJohn = new StudentBuilder(firstJohn).withId("E9191919").build();
        model.addStudent(firstJohn);
        model.addStudent(secondJohn);

        Group groupToAdd = new GroupBuilder().withName(NEW_GROUP).build();
        AllocDescriptor johnDoeNameDescriptor = new AllocDescriptorBuilder()
                .withGroup(NEW_GROUP)
                .withName(duplicatedName).build();

        AddGroupCommand command = new AddGroupCommand(groupToAdd, Arrays.asList(johnDoeNameDescriptor));

        assertThrows(CommandException.class, String.format(AddGroupCommand.MESSAGE_DUPLICATE_STUDENT,
                duplicatedName), () -> command.execute(model));
    }

    @Test
    public void formatSuccessMessage_noStudentsToAdd_returnsGroupAddedMessage() {
        Group standardGroup = new GroupBuilder().withName(VALID_GROUP_TUTORIAL).build();
        AddGroupCommand standardCommand = new AddGroupCommand(standardGroup, new ArrayList<>());

        // no students to add -> returns only group added message
        assertEquals(standardCommand.formatSuccessMessage(new ArrayList<>()),
                String.format(AddGroupCommand.MESSAGE_SUCCESS, standardGroup.name));
    }

    @Test
    public void formatSuccessMessage_studentsToAdd_returnsGroupAndStudentsAddedMessage() {
        Group standardGroup = new GroupBuilder().withName(VALID_GROUP_TUTORIAL).build();
        AddGroupCommand standardCommand = new AddGroupCommand(standardGroup, new ArrayList<>());

        // one student to add
        List<Student> listWithOneStudent = Arrays.asList(AMY);
        assertEquals(standardCommand.formatSuccessMessage(listWithOneStudent),
                String.format(AddGroupCommand.MESSAGE_SUCCESS, standardGroup.name)
                        + String.format(AddGroupCommand.MESSAGE_STUDENTS_ADDED, AMY.getName()));

        // multiple students to add
        List<Student> listWithMultipleStudents = Arrays.asList(AMY, BOB);
        String names = AMY.getName().fullName + ", " + BOB.getName().fullName;
        assertEquals(standardCommand.formatSuccessMessage(listWithMultipleStudents),
                String.format(AddGroupCommand.MESSAGE_SUCCESS, standardGroup.name)
                        + String.format(AddGroupCommand.MESSAGE_STUDENTS_ADDED, names));
    }

    @Test
    public void equals() {
        Group standardGroup = new GroupBuilder().withName(VALID_GROUP_TUTORIAL).build();
        AllocDescriptor standardDescriptor = new AllocDescriptorBuilder()
                .withGroup(VALID_GROUP_TUTORIAL)
                .withName(VALID_NAME_AMY).build();

        final AddGroupCommand standardCommand = new AddGroupCommand(standardGroup, Arrays.asList(standardDescriptor));

        // same values -> returns true
        AllocDescriptor copyDescriptor = new AllocDescriptorBuilder(standardDescriptor).build();
        Group copyGroup = new GroupBuilder(standardGroup).build();
        AddGroupCommand commandWithSameValues = new AddGroupCommand(copyGroup, Arrays.asList(copyDescriptor));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different group -> returns false
        Group differentGroup = new GroupBuilder().withName(VALID_GROUP_RECITATION).build();
        assertFalse(standardCommand.equals(new AddGroupCommand(differentGroup, Arrays.asList(standardDescriptor))));

        // different descriptor -> returns false
        AllocDescriptor differentDescriptor = new AllocDescriptorBuilder()
                .withGroup(VALID_GROUP_RECITATION)
                .withName(VALID_NAME_BOB).build();
        assertFalse(standardCommand.equals(new AddGroupCommand(standardGroup, Arrays.asList(differentDescriptor))));
    }
}
