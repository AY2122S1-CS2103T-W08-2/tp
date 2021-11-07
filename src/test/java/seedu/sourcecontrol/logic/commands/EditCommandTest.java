package seedu.sourcecontrol.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.EDIT_DESCRIPTOR_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.EDIT_DESCRIPTOR_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.showStudentAtIndex;
import static seedu.sourcecontrol.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.sourcecontrol.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.sourcecontrol.testutil.TypicalStudents.getTypicalSourceControl;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.commons.core.index.Index;
import seedu.sourcecontrol.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ModelManager;
import seedu.sourcecontrol.model.SourceControl;
import seedu.sourcecontrol.model.UserPrefs;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.testutil.EditStudentDescriptorBuilder;
import seedu.sourcecontrol.testutil.StudentBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private final Model model = new ModelManager(getTypicalSourceControl(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Student editedStudent = new StudentBuilder().build();
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder(editedStudent).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_STUDENT, descriptor);

        editedStudent.addScores(model.getFilteredStudentList().get(0).getScores());
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_STUDENT_SUCCESS, editedStudent);

        Model expectedModel = new ModelManager(new SourceControl(model.getSourceControl()), new UserPrefs());
        expectedModel.setStudent(model.getFilteredStudentList().get(0), editedStudent);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastStudent = Index.fromOneBased(model.getFilteredStudentList().size());
        Student lastStudent = model.getFilteredStudentList().get(indexLastStudent.getZeroBased());

        StudentBuilder studentInList = new StudentBuilder(lastStudent);
        Student editedStudent = studentInList.withName(VALID_NAME_BOB).withId(VALID_ID_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withName(VALID_NAME_BOB)
                .withId(VALID_ID_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastStudent, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_STUDENT_SUCCESS, editedStudent);

        Model expectedModel = new ModelManager(new SourceControl(model.getSourceControl()), new UserPrefs());
        expectedModel.setStudent(lastStudent, editedStudent);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_STUDENT, new EditStudentDescriptor());
        Student editedStudent = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_STUDENT_SUCCESS, editedStudent);

        Model expectedModel = new ModelManager(new SourceControl(model.getSourceControl()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);

        Student studentInFilteredList = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Student editedStudent = new StudentBuilder(studentInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_STUDENT,
                new EditStudentDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_STUDENT_SUCCESS, editedStudent);

        Model expectedModel = new ModelManager(new SourceControl(model.getSourceControl()), new UserPrefs());
        expectedModel.setStudent(model.getFilteredStudentList().get(0), editedStudent);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateStudentUnfilteredList_failure() {
        Student firstStudent = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder(firstStudent).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_STUDENT, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_STUDENT);
    }

    @Test
    public void execute_duplicateStudentFilteredList_failure() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);

        // edit student in filtered list into a duplicate in source control
        Student studentInList = model.getSourceControl().getStudentList().get(INDEX_SECOND_STUDENT.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_STUDENT,
                new EditStudentDescriptorBuilder(studentInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_STUDENT);
    }

    @Test
    public void execute_nonexistentGroup_failure() {
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withGroups("poqwepq").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_STUDENT, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_NONEXISTENT_GROUP);
    }

    @Test
    public void execute_invalidStudentIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model,
                String.format(MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX, model.getFilteredStudentList().size()));
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of source control's student list
     */
    @Test
    public void execute_invalidStudentIndexFilteredList_failure() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);
        Index outOfBoundIndex = INDEX_SECOND_STUDENT;
        // ensures that outOfBoundIndex is still in bounds of source control student list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getSourceControl().getStudentList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditStudentDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model,
                String.format(MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX, model.getFilteredStudentList().size()));
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_STUDENT, EDIT_DESCRIPTOR_AMY);

        // same values -> returns true
        EditStudentDescriptor copyDescriptor = new EditStudentDescriptor(EDIT_DESCRIPTOR_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_STUDENT, copyDescriptor);
        assertEquals(standardCommand, commandWithSameValues);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(standardCommand, null);

        // different types -> returns false
        assertNotEquals(standardCommand, new ClearCommand());

        // different index -> returns false
        assertNotEquals(standardCommand, new EditCommand(INDEX_SECOND_STUDENT, EDIT_DESCRIPTOR_AMY));

        // different descriptor -> returns false
        assertNotEquals(standardCommand, new EditCommand(INDEX_FIRST_STUDENT, EDIT_DESCRIPTOR_BOB));
    }
}

