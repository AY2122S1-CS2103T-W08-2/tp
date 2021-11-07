package seedu.sourcecontrol.logic.parser;

import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ASSESSMENT_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.FILE_DESC_VALID_FILE;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.GROUP_DESC_TUTORIAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ID_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_ASSESSMENT_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_GROUP_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_ID_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ASSESSMENT_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_TUTORIAL;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.sourcecontrol.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.sourcecontrol.testutil.TypicalStudents.AMY;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.commons.core.index.Index;
import seedu.sourcecontrol.logic.commands.ShowCommand;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.testutil.AssessmentBuilder;
import seedu.sourcecontrol.testutil.GroupBuilder;

public class ShowCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowCommand.MESSAGE_USAGE);

    private final ShowCommandParser parser = new ShowCommandParser();

    private final Index index = INDEX_SECOND_STUDENT;
    private final Student student = AMY;
    private final Assessment assessment = new AssessmentBuilder().withValue(VALID_ASSESSMENT_AMY).build();
    private final Group group = new GroupBuilder().withName(VALID_GROUP_TUTORIAL).build();

    private final Path savePath = parser.generateNewPath(0); // added when graph is exported

    @Test
    public void parse_nullPath() {
        // parse by index
        assertParseSuccess(parser, String.valueOf(index.getOneBased()), new ShowCommand(index, null));

        // parse by prefixes
        assertParseSuccess(parser, NAME_DESC_AMY, new ShowCommand(student.getName(), null));
        assertParseSuccess(parser, ID_DESC_AMY, new ShowCommand(student.getId(), null));
        assertParseSuccess(parser, ASSESSMENT_DESC_AMY, new ShowCommand(assessment, null));
        assertParseSuccess(parser, GROUP_DESC_TUTORIAL, new ShowCommand(group, null));
    }

    @Test
    public void parse_presentPath() {
        // parse by index
        assertParseSuccess(parser, index.getOneBased() + FILE_DESC_VALID_FILE, new ShowCommand(index, savePath));

        // parse by prefixes
        assertParseSuccess(parser, NAME_DESC_AMY + FILE_DESC_VALID_FILE, new ShowCommand(student.getName(), savePath));
        assertParseSuccess(parser, ID_DESC_AMY + FILE_DESC_VALID_FILE, new ShowCommand(student.getId(), savePath));
        assertParseSuccess(parser, ASSESSMENT_DESC_AMY + FILE_DESC_VALID_FILE, new ShowCommand(assessment, savePath));
        assertParseSuccess(parser, GROUP_DESC_TUTORIAL + FILE_DESC_VALID_FILE, new ShowCommand(group, savePath));
    }

    @Test
    public void parseByIndex_validIndex_success() {
        assertParseSuccess(parser, index.getOneBased() + FILE_DESC_VALID_FILE, new ShowCommand(index, savePath));
    }

    @Test
    public void parseByIndex_invalidIndex_failure() {
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT); // empty preamble
        assertParseFailure(parser, "-5", ParserUtil.MESSAGE_INVALID_INDEX); // negative index
        assertParseFailure(parser, "0", ParserUtil.MESSAGE_INVALID_INDEX); // zero index
        assertParseFailure(parser, "2147483648", ParserUtil.MESSAGE_INVALID_INDEX); // MAX_INT + 1 index
    }

    @Test
    public void parseByPrefixes_validName_success() {
        assertParseSuccess(parser, NAME_DESC_AMY + FILE_DESC_VALID_FILE, new ShowCommand(student.getName(), savePath));
    }

    @Test
    public void parseByPrefixes_invalidName_failure() {
        assertParseFailure(parser, INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parseByPrefixes_validId_success() {
        assertParseSuccess(parser, ID_DESC_AMY + FILE_DESC_VALID_FILE, new ShowCommand(student.getId(), savePath));
    }

    @Test
    public void parseByPrefixes_invalidId_failure() {
        assertParseFailure(parser, INVALID_ID_DESC, Id.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parseByPrefixes_validAssessment_success() {
        assertParseSuccess(parser, ASSESSMENT_DESC_AMY + FILE_DESC_VALID_FILE, new ShowCommand(assessment, savePath));
    }

    @Test
    public void parseByPrefixes_invalidAssessment_failure() {
        assertParseFailure(parser, INVALID_ASSESSMENT_DESC, Assessment.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parseByPrefixes_validGroup_success() {
        assertParseSuccess(parser, GROUP_DESC_TUTORIAL + FILE_DESC_VALID_FILE, new ShowCommand(group, savePath));
    }

    @Test
    public void parseByPrefixes_invalidGroup_failure() {
        assertParseFailure(parser, INVALID_GROUP_DESC, Group.MESSAGE_CONSTRAINTS);
    }
}
