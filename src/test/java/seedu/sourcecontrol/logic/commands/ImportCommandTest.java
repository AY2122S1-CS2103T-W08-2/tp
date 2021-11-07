package seedu.sourcecontrol.logic.commands;

import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NON_EXISTENT_PATH;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TYPICAL_STUDENTS_CSV_PATH;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TYPICAL_STUDENTS_GROUP_COUNT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TYPICAL_STUDENTS_TAG_COUNT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_WRONG_CSV_PATH;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ModelManager;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.Score;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.model.student.tag.Tag;
import seedu.sourcecontrol.testutil.TypicalStudents;

public class ImportCommandTest {
    private static final String PATH_FORMAT = "src/test/data/ImportCommandTest/%s.csv";

    @Test
    public void execute_validFile_success() {
        ImportCommand command = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                Path.of(VALID_TYPICAL_STUDENTS_CSV_PATH));

        Model actualModel = new ModelManager();
        Model expectedModel = new ModelManager();
        expectedModel.setSourceControl(TypicalStudents.getTypicalSourceControl());

        assertCommandSuccess(command, actualModel, ImportCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_missingColumn_failure() {
        ImportCommand command = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                Path.of(VALID_WRONG_CSV_PATH));

        assertCommandFailure(command, new ModelManager(), ImportCommand.MESSAGE_OUT_OF_BOUNDS);
    }

    @Test
    public void execute_missingFile_failure() {
        ImportCommand command = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                Path.of(VALID_NON_EXISTENT_PATH));

        assertCommandFailure(command, new ModelManager(), ImportCommand.MESSAGE_INVALID_FILE);
    }

    @Test
    public void execute_badData_failure() {
        ImportCommand command = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                Path.of(String.format(PATH_FORMAT, "wrongAssessmentName")));
        assertCommandFailure(command, new ModelManager(), Assessment.MESSAGE_CONSTRAINTS);

        command = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                Path.of(String.format(PATH_FORMAT, "wrongId")));
        assertCommandFailure(command, new ModelManager(), Id.MESSAGE_CONSTRAINTS);

        command = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                Path.of(String.format(PATH_FORMAT, "wrongName")));
        assertCommandFailure(command, new ModelManager(), Name.MESSAGE_CONSTRAINTS);

        command = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                Path.of(String.format(PATH_FORMAT, "wrongGroup")));
        assertCommandFailure(command, new ModelManager(), Group.MESSAGE_CONSTRAINTS);

        command = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                Path.of(String.format(PATH_FORMAT, "wrongScore")));
        assertCommandFailure(command, new ModelManager(), Score.MESSAGE_CONSTRAINTS);

        command = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                Path.of(String.format(PATH_FORMAT, "wrongTag")));
        assertCommandFailure(command, new ModelManager(), Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void execute_missingData_failure() {
        ImportCommand command = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                Path.of(String.format(PATH_FORMAT, "missingAssessmentName")));
        assertCommandFailure(command, new ModelManager(), Assessment.MESSAGE_CONSTRAINTS);

        command = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                Path.of(String.format(PATH_FORMAT, "missingId")));
        assertCommandFailure(command, new ModelManager(), Id.MESSAGE_CONSTRAINTS);

        command = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                Path.of(String.format(PATH_FORMAT, "missingName")));
        assertCommandFailure(command, new ModelManager(), Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void execute_duplicateData_failure() {
        ImportCommand command = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                Path.of(String.format(PATH_FORMAT, "duplicateAssessmentName")));
        assertCommandFailure(command, new ModelManager(), ImportCommand.MESSAGE_DUPLICATE_ASSESSMENT);

        command = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                Path.of(String.format(PATH_FORMAT, "duplicateId")));
        assertCommandFailure(command, new ModelManager(), ImportCommand.MESSAGE_DUPLICATE_ID);
    }
}
