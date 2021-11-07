package seedu.sourcecontrol.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.showStudentAtIndex;
import static seedu.sourcecontrol.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.sourcecontrol.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.sourcecontrol.testutil.TypicalStudents.getTypicalSourceControl;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.commons.core.index.Index;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ModelManager;
import seedu.sourcecontrol.model.UserPrefs;
import seedu.sourcecontrol.model.student.Student;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalSourceControl(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Student studentToDelete = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_STUDENT);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_STUDENT_SUCCESS, studentToDelete);

        ModelManager expectedModel = new ModelManager(model.getSourceControl(), new UserPrefs());
        expectedModel.deleteStudent(studentToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model,
                String.format(MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX, model.getFilteredStudentList().size()));
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);

        Student studentToDelete = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_STUDENT);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_STUDENT_SUCCESS, studentToDelete);

        Model expectedModel = new ModelManager(model.getSourceControl(), new UserPrefs());
        expectedModel.deleteStudent(studentToDelete);
        showNoStudent(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);

        Index outOfBoundIndex = INDEX_SECOND_STUDENT;
        // ensures that outOfBoundIndex is still in bounds of student list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getSourceControl().getStudentList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model,
                String.format(MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX, model.getFilteredStudentList().size()));
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_STUDENT);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_STUDENT);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_STUDENT);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different student -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoStudent(Model model) {
        model.updateFilteredStudentList(p -> false);

        assertTrue(model.getFilteredStudentList().isEmpty());
    }
}
