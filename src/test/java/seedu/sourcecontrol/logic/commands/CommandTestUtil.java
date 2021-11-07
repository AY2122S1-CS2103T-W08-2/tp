package seedu.sourcecontrol.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ASSESSMENT;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_FILE;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_SCORE;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import seedu.sourcecontrol.commons.core.index.Index;
import seedu.sourcecontrol.logic.commands.exceptions.CommandException;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.SourceControl;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.Score;
import seedu.sourcecontrol.model.student.name.NameContainsKeywordsPredicate;
import seedu.sourcecontrol.model.student.name.NameEqualsPredicate;
import seedu.sourcecontrol.testutil.AllocDescriptorBuilder;
import seedu.sourcecontrol.testutil.EditStudentDescriptorBuilder;
import seedu.sourcecontrol.testutil.ScoreDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_ID_AMY = "E0543947";
    public static final String VALID_ID_BOB = "E0639293";
    public static final String VALID_GROUP_TUTORIAL = "T08D";
    public static final String VALID_GROUP_RECITATION = "R07B";
    public static final String VALID_ASSESSMENT_AMY = "P05";
    public static final String VALID_ASSESSMENT_BOB = "M01";
    public static final String VALID_SCORE_AMY = "100";
    public static final String VALID_SCORE_BOB = "66.6";
    public static final Map<Assessment, Score> VALID_SCORES_AMY =
            Map.of(new Assessment(VALID_ASSESSMENT_AMY), new Score(VALID_SCORE_AMY));
    public static final Map<Assessment, Score> VALID_SCORES_BOB =
            Map.of(new Assessment(VALID_ASSESSMENT_BOB), new Score(VALID_SCORE_BOB));
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_TYPICAL_STUDENTS_CSV_PATH = "src/test/data/ImportCommandTest/typicalStudents.csv";
    public static final String VALID_WRONG_CSV_PATH = "src/test/data/ImportCommandTest/wrongFormat.csv";
    public static final String VALID_NON_EXISTENT_PATH = "src/test/data/ImportCommandTest/doesNotExist.csv";
    public static final int VALID_TYPICAL_STUDENTS_GROUP_COUNT = 2;
    public static final int VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT = 2;
    public static final int VALID_TYPICAL_STUDENTS_TAG_COUNT = 2;

    public static final String ASSESSMENT_DESC_AMY = " " + PREFIX_ASSESSMENT + VALID_ASSESSMENT_AMY;
    public static final String ASSESSMENT_DESC_BOB = " " + PREFIX_ASSESSMENT + VALID_ASSESSMENT_BOB;
    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String ID_DESC_AMY = " " + PREFIX_ID + VALID_ID_AMY;
    public static final String ID_DESC_BOB = " " + PREFIX_ID + VALID_ID_BOB;
    public static final String GROUP_DESC_TUTORIAL = " " + PREFIX_GROUP + VALID_GROUP_TUTORIAL;
    public static final String GROUP_DESC_RECITATION = " " + PREFIX_GROUP + VALID_GROUP_RECITATION;
    public static final String SCORE_DESC_AMY = " " + PREFIX_SCORE + VALID_SCORE_AMY;
    public static final String SCORE_DESC_BOB = " " + PREFIX_SCORE + VALID_SCORE_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;

    public static final String FILE_DESC_VALID_FILE = " " + PREFIX_FILE + VALID_TYPICAL_STUDENTS_CSV_PATH;
    public static final String FILE_DESC_INVALID_FILE = " " + PREFIX_FILE + VALID_WRONG_CSV_PATH;
    public static final String FILE_DESC_NON_EXISTENT_FILE = " " + PREFIX_FILE + VALID_NON_EXISTENT_PATH;
    public static final String GROUP_COUNT_DESC_TYPICAL = " " + PREFIX_GROUP + VALID_TYPICAL_STUDENTS_GROUP_COUNT;
    public static final String ASSESSMENT_COUNT_DESC_TYPICAL = " " + PREFIX_ASSESSMENT
            + VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT;
    public static final String TAG_COUNT_DESC_TYPICAL = " " + PREFIX_TAG + VALID_TYPICAL_STUDENTS_TAG_COUNT;
    public static final String TAG_COUNT_DESC_ABOVE_TYPICAL = " " + PREFIX_TAG + (VALID_TYPICAL_STUDENTS_TAG_COUNT + 1);

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_ID_DESC = " " + PREFIX_ID + "E0123"; // ID length too short
    public static final String INVALID_GROUP_DESC = " " + PREFIX_GROUP + "05&"; // '&' not allowed in names
    public static final String INVALID_ASSESSMENT_DESC = " " + PREFIX_ASSESSMENT + "P01&"; // '&' not allowed in name
    public static final String INVALID_SCORE_DESC = " " + PREFIX_SCORE + "invalid0"; // only numbers
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags
    public static final String INVALID_GROUP_COUNT_DESC = " " + PREFIX_GROUP + "invalid1"; // only numbers allowed
    public static final String INVALID_ASSESSMENT_COUNT_DESC = " " + PREFIX_ASSESSMENT + "invalid2"; // only numbers
    public static final String INVALID_TAG_COUNT_DESC = " " + PREFIX_TAG + "invalid3"; // only numbers

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final AddAllocCommand.AllocDescriptor ALLOC_DESCRIPTOR_AMY;
    public static final AddAllocCommand.AllocDescriptor ALLOC_DESCRIPTOR_BOB;

    static {
        ALLOC_DESCRIPTOR_AMY = new AllocDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withGroup(VALID_GROUP_TUTORIAL).build();
        ALLOC_DESCRIPTOR_BOB = new AllocDescriptorBuilder()
                .withId(VALID_ID_BOB)
                .withGroup(VALID_GROUP_RECITATION).build();
    }

    public static final AddScoreCommand.ScoreDescriptor SCORE_DESCRIPTOR_AMY;
    public static final AddScoreCommand.ScoreDescriptor SCORE_DESCRIPTOR_BOB;

    static {
        SCORE_DESCRIPTOR_AMY = new ScoreDescriptorBuilder()
                .withAssessment(VALID_ASSESSMENT_AMY)
                .withName(VALID_NAME_AMY)
                .withScore(VALID_SCORE_AMY).build();
        SCORE_DESCRIPTOR_BOB = new ScoreDescriptorBuilder()
                .withAssessment(VALID_ASSESSMENT_BOB)
                .withId(VALID_ID_BOB)
                .withScore(VALID_SCORE_BOB).build();
    }

    public static final EditCommand.EditStudentDescriptor EDIT_DESCRIPTOR_AMY;
    public static final EditCommand.EditStudentDescriptor EDIT_DESCRIPTOR_BOB;

    static {
        EDIT_DESCRIPTOR_AMY = new EditStudentDescriptorBuilder().withName(VALID_NAME_AMY)
                .withId(VALID_ID_AMY)
                .withGroups(VALID_GROUP_TUTORIAL)
                .withTags(VALID_TAG_FRIEND).build();
        EDIT_DESCRIPTOR_BOB = new EditStudentDescriptorBuilder().withName(VALID_NAME_BOB)
                .withId(VALID_ID_BOB)
                .withGroups(VALID_GROUP_RECITATION)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the Source Control, filtered student list and selected student in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        SourceControl expectedSourceControl = new SourceControl(actualModel.getSourceControl());
        List<Student> expectedFilteredList = new ArrayList<>(actualModel.getFilteredStudentList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedSourceControl, actualModel.getSourceControl());
        assertEquals(expectedFilteredList, actualModel.getFilteredStudentList());
    }

    /**
     * Executes the given command, confirms that CommandException is thrown, and the CommandException
     * message matches the expected Message. The Source Control, and selected student in actualModel remains unchanged.
     * Filtered student list changes and thus this method checks if actual filtered list matches that of the
     * expectedFilteredList.
     * @param command command to be executed.
     * @param actualModel actual state of the model.
     * @param expectedMessage expected error message.
     * @param expectedModel expected Model after executing command.
     */
    public static void assertCommandFailureWithFilteredListChange(Command command, Model actualModel,
                                                                  String expectedMessage, Model expectedModel,
                                                                  String studentName) {
        SourceControl expectedSourceControl = new SourceControl(expectedModel.getSourceControl());
        expectedModel.updateFilteredStudentList(new NameEqualsPredicate(studentName));
        List<Student> expectedFilteredList = new ArrayList<>(expectedModel.getFilteredStudentList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedSourceControl, actualModel.getSourceControl());
        assertEquals(expectedFilteredList, actualModel.getFilteredStudentList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the student at the given {@code targetIndex} in the
     * {@code model}'s Source Control.
     */
    public static void showStudentAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredStudentList().size());

        Student student = model.getFilteredStudentList().get(targetIndex.getZeroBased());
        final String[] splitName = student.getName().fullName.split("\\s+");
        model.updateFilteredStudentList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredStudentList().size());
    }

}
