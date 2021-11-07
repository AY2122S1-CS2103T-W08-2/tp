package seedu.sourcecontrol.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ID_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;
import static seedu.sourcecontrol.testutil.TypicalStudents.AMY;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.sourcecontrol.commons.core.GuiSettings;
import seedu.sourcecontrol.logic.commands.AddStudentCommand;
import seedu.sourcecontrol.logic.commands.CommandResult;
import seedu.sourcecontrol.logic.commands.ListCommand;
import seedu.sourcecontrol.logic.commands.exceptions.CommandException;
import seedu.sourcecontrol.logic.parser.exceptions.ParseException;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ModelManager;
import seedu.sourcecontrol.model.ReadOnlySourceControl;
import seedu.sourcecontrol.model.UserPrefs;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.storage.JsonSourceControlStorage;
import seedu.sourcecontrol.storage.JsonUserPrefsStorage;
import seedu.sourcecontrol.storage.StorageManager;
import seedu.sourcecontrol.testutil.StudentBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonSourceControlStorage sourceControlStorage =
                new JsonSourceControlStorage(temporaryFolder.resolve("sourceControl.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(sourceControlStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand,
                String.format(MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX, model.getFilteredStudentList().size()));
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        // Setup LogicManager with JsonSourceControlIoExceptionThrowingStub
        JsonSourceControlStorage sourceControlStorage =
                new JsonSourceControlIoExceptionThrowingStub(temporaryFolder.resolve("ioExceptionSourceControl.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ioExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(sourceControlStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);

        // Execute add command
        String addCommand = AddStudentCommand.COMMAND_WORD + NAME_DESC_AMY + ID_DESC_AMY;
        Student expectedStudent = new StudentBuilder(AMY)
                .withGroups()
                .withScores(new LinkedHashMap<>())
                .withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addStudent(expectedStudent);
        String expectedMessage = LogicManager.FILE_OPS_ERROR_MESSAGE + DUMMY_IO_EXCEPTION;
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    @Test
    public void getFilteredStudentList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredStudentList().remove(0));
    }

    @Test
    public void getSourceControl() {
        assertSame(logic.getSourceControl(), model.getSourceControl());
    }

    @Test
    public void getSourceControlFilePath() {
        assertSame(logic.getSourceControlFilePath(), model.getSourceControlFilePath());
    }

    @Test
    public void getGuiSettings() {
        assertSame(logic.getGuiSettings(), model.getGuiSettings());
    }

    @Test
    public void setGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(100, 100, 100, 100, 100);
        logic.setGuiSettings(guiSettings);
        assertSame(logic.getGuiSettings(), guiSettings);
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getSourceControl(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * A stub class to throw an {@code IOException} when the save method is called.
     */
    private static class JsonSourceControlIoExceptionThrowingStub extends JsonSourceControlStorage {
        private JsonSourceControlIoExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveSourceControl(ReadOnlySourceControl sourceControl, Path filePath) throws IOException {
            throw DUMMY_IO_EXCEPTION;
        }
    }
}
