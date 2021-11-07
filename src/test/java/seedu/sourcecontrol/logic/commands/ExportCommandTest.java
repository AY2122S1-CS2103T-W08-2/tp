package seedu.sourcecontrol.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TYPICAL_STUDENTS_GROUP_COUNT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TYPICAL_STUDENTS_TAG_COUNT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.sourcecontrol.testutil.TypicalStudents.getTypicalSourceControl;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.sourcecontrol.commons.util.FileUtil;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ModelManager;
import seedu.sourcecontrol.model.SourceControl;
import seedu.sourcecontrol.model.UserPrefs;

public class ExportCommandTest {
    @TempDir
    public Path testFolder;

    private final Model model = new ModelManager(getTypicalSourceControl(), new UserPrefs());
    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void execute_success() {
        Path path = getTempFilePath("execute.csv");
        ExportCommand command = new ExportCommand();
        command.setFile(path);
        Model expectedModel = new ModelManager(getTypicalSourceControl(), new UserPrefs());
        assertCommandSuccess(command, model,
                String.format(ExportCommand.MESSAGE_SUCCESS, path.toString()), expectedModel);
    }

    @Test
    public void exportImport_sameAb_success() throws Exception {
        Path path = getTempFilePath("exportImport.csv");
        ExportCommand command = new ExportCommand();
        command.setFile(path);
        command.execute(model);

        ImportCommand importCommand = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT, path);

        Model expectedModel = new ModelManager(new SourceControl(), new UserPrefs());
        importCommand.execute(expectedModel);

        assertEquals(model, expectedModel);
    }

    @Test
    public void equals() {
        ExportCommand command = new ExportCommand();
        ExportCommand otherCommand = new ExportCommand();

        // same object
        assertEquals(command, command);

        // same path
        assertEquals(otherCommand, command);

        otherCommand.setFile(Path.of("abc.csv"));
        // different path
        assertNotEquals(otherCommand, command);

        // null
        assertNotEquals(command, null);

        // different commands
        assertNotEquals(new ClearCommand(), command);

        // different type
        assertNotEquals(command, 1);
    }

    @Test
    public void generatePath_success() {
        assertEquals(new ExportCommand().getFile(), FileUtil.pathOf("sourceControl.csv"));

        assertEquals(ExportCommand.generateNewPath(0), FileUtil.pathOf("sourceControl.csv"));
        assertEquals(ExportCommand.generateNewPath(1), FileUtil.pathOf("sourceControl(1).csv"));
    }
}
