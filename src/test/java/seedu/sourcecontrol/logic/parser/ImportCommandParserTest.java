package seedu.sourcecontrol.logic.parser;

import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ASSESSMENT_COUNT_DESC_TYPICAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.FILE_DESC_INVALID_FILE;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.FILE_DESC_NON_EXISTENT_FILE;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.FILE_DESC_VALID_FILE;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.GROUP_COUNT_DESC_TYPICAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_ASSESSMENT_COUNT_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_GROUP_COUNT_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_TAG_COUNT_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.TAG_COUNT_DESC_ABOVE_TYPICAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.TAG_COUNT_DESC_TYPICAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NON_EXISTENT_PATH;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TYPICAL_STUDENTS_CSV_PATH;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TYPICAL_STUDENTS_GROUP_COUNT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TYPICAL_STUDENTS_TAG_COUNT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_WRONG_CSV_PATH;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_FILE;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.logic.commands.ImportCommand;

public class ImportCommandParserTest {
    private ImportCommandParser parser = new ImportCommandParser();

    @Test
    public void parse_allFieldsPresent_success() throws Exception {
        ImportCommand expectedCommand = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                ParserUtil.parsePath(VALID_TYPICAL_STUDENTS_CSV_PATH, ".csv"));

        // standard usage
        assertParseSuccess(parser, FILE_DESC_VALID_FILE
                + GROUP_COUNT_DESC_TYPICAL + ASSESSMENT_COUNT_DESC_TYPICAL + TAG_COUNT_DESC_TYPICAL, expectedCommand);

        // with whitespace preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + FILE_DESC_VALID_FILE
                + GROUP_COUNT_DESC_TYPICAL + ASSESSMENT_COUNT_DESC_TYPICAL + TAG_COUNT_DESC_TYPICAL, expectedCommand);

        // multiple arguments provided, only last one used
        assertParseSuccess(parser, FILE_DESC_NON_EXISTENT_FILE + FILE_DESC_VALID_FILE
                + INVALID_GROUP_COUNT_DESC + GROUP_COUNT_DESC_TYPICAL
                + INVALID_ASSESSMENT_COUNT_DESC + ASSESSMENT_COUNT_DESC_TYPICAL
                + INVALID_TAG_COUNT_DESC + TAG_COUNT_DESC_TYPICAL, expectedCommand);
    }

    @Test
    public void parse_someFieldsPresent_success() throws Exception {
        ImportCommand expectedCommand = new ImportCommand(
                0,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                ParserUtil.parsePath(VALID_TYPICAL_STUDENTS_CSV_PATH, ".csv"));

        // group count not provided
        assertParseSuccess(parser, FILE_DESC_VALID_FILE
                + ASSESSMENT_COUNT_DESC_TYPICAL + TAG_COUNT_DESC_TYPICAL, expectedCommand);

        expectedCommand = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                0,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                ParserUtil.parsePath(VALID_TYPICAL_STUDENTS_CSV_PATH, ".csv"));

        // assessment count not provided
        assertParseSuccess(parser, FILE_DESC_VALID_FILE
                + GROUP_COUNT_DESC_TYPICAL + TAG_COUNT_DESC_TYPICAL, expectedCommand);

        expectedCommand = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                0,
                ParserUtil.parsePath(VALID_TYPICAL_STUDENTS_CSV_PATH, ".csv"));

        // tag count not provided
        assertParseSuccess(parser, FILE_DESC_VALID_FILE
                + GROUP_COUNT_DESC_TYPICAL + ASSESSMENT_COUNT_DESC_TYPICAL, expectedCommand);

        expectedCommand = new ImportCommand(0, 0, 0,
                ParserUtil.parsePath(VALID_TYPICAL_STUDENTS_CSV_PATH, ".csv"));

        // only file provided
        assertParseSuccess(parser, FILE_DESC_VALID_FILE, expectedCommand);
    }

    @Test
    public void parse_fileNotPresent_failure() {
        assertParseFailure(parser, GROUP_COUNT_DESC_TYPICAL + ASSESSMENT_COUNT_DESC_TYPICAL
                + TAG_COUNT_DESC_TYPICAL, String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyFileName_failure() {
        assertParseFailure(parser, " " + PREFIX_FILE, ParserUtil.MESSAGE_EMPTY_PATH);
    }

    @Test
    public void parse_tooManyColumnsSpecified_success() throws Exception {
        ImportCommand expectedCommand = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT + 1,
                ParserUtil.parsePath(VALID_TYPICAL_STUDENTS_CSV_PATH, ".csv"));

        // It's the command's job to read the file and validate column counts
        assertParseSuccess(parser, FILE_DESC_VALID_FILE + GROUP_COUNT_DESC_TYPICAL
                + ASSESSMENT_COUNT_DESC_TYPICAL + TAG_COUNT_DESC_ABOVE_TYPICAL, expectedCommand);
    }

    @Test
    public void parse_invalidArguments_failure() {
        // Not a number
        assertParseFailure(parser, FILE_DESC_VALID_FILE + INVALID_GROUP_COUNT_DESC
                + ASSESSMENT_COUNT_DESC_TYPICAL + TAG_COUNT_DESC_TYPICAL, ImportCommand.MESSAGE_INVALID_NUMBER);

        assertParseFailure(parser, FILE_DESC_VALID_FILE + GROUP_COUNT_DESC_TYPICAL
                + INVALID_ASSESSMENT_COUNT_DESC + TAG_COUNT_DESC_TYPICAL, ImportCommand.MESSAGE_INVALID_NUMBER);

        assertParseFailure(parser, FILE_DESC_VALID_FILE + GROUP_COUNT_DESC_TYPICAL
                + ASSESSMENT_COUNT_DESC_TYPICAL + INVALID_TAG_COUNT_DESC, ImportCommand.MESSAGE_INVALID_NUMBER);
    }

    @Test
    public void parse_badFile_success() throws Exception {
        ImportCommand expectedCommand = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                ParserUtil.parsePath(VALID_NON_EXISTENT_PATH, ".csv"));

        // path leads to a file that doesn't exist (it's the command's job to check for existence)
        assertParseSuccess(parser, FILE_DESC_NON_EXISTENT_FILE
                + GROUP_COUNT_DESC_TYPICAL + ASSESSMENT_COUNT_DESC_TYPICAL + TAG_COUNT_DESC_TYPICAL, expectedCommand);

        expectedCommand = new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                ParserUtil.parsePath(VALID_WRONG_CSV_PATH, ".csv"));

        // path leads to a file that has the wrong format (it's the command's job to read the file)
        assertParseSuccess(parser, FILE_DESC_INVALID_FILE
                + GROUP_COUNT_DESC_TYPICAL + ASSESSMENT_COUNT_DESC_TYPICAL + TAG_COUNT_DESC_TYPICAL, expectedCommand);
    }

    @Test
    public void parse_invalidPath_failure() {
        assertParseFailure(parser, " -f abc.png", String.format(ParserUtil.MESSAGE_WRONG_EXTENSION, ".csv"));

        assertParseFailure(parser, " -f .", ParserUtil.MESSAGE_DIRECTORY);

        assertParseFailure(parser, " -f ///\0.csv", ParserUtil.MESSAGE_INVALID_PATH);

        assertParseFailure(parser, " -f ", ParserUtil.MESSAGE_EMPTY_PATH);
    }
}
