package seedu.sourcecontrol.logic.parser;

import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.GROUP_DESC_RECITATION;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.GROUP_DESC_TUTORIAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ID_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ID_DESC_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_GROUP_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_ID_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_RECITATION;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_TUTORIAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.sourcecontrol.testutil.TypicalStudents.AMY;
import static seedu.sourcecontrol.testutil.TypicalStudents.BOB;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.logic.commands.AddStudentCommand;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.model.student.tag.Tag;
import seedu.sourcecontrol.testutil.StudentBuilder;

public class AddStudentCommandParserTest {
    private AddStudentCommandParser parser = new AddStudentCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Student expectedStudent = new StudentBuilder(BOB)
                .withGroups(VALID_GROUP_TUTORIAL)
                .withScores(new LinkedHashMap<>())
                .withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + ID_DESC_BOB + GROUP_DESC_TUTORIAL
                + TAG_DESC_FRIEND, new AddStudentCommand(expectedStudent));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + ID_DESC_BOB + GROUP_DESC_TUTORIAL
                + TAG_DESC_FRIEND, new AddStudentCommand(expectedStudent));

        // multiple IDs - last Id accepted
        assertParseSuccess(parser, NAME_DESC_BOB + ID_DESC_AMY + ID_DESC_BOB + GROUP_DESC_TUTORIAL
                + TAG_DESC_FRIEND, new AddStudentCommand(expectedStudent));

        // multiple groups - all accepted
        Student expectedStudentMultipleGroups = new StudentBuilder(BOB)
                .withScores(new LinkedHashMap<>())
                .withGroups(VALID_GROUP_TUTORIAL, VALID_GROUP_RECITATION)
                .withTags(VALID_TAG_FRIEND).build();
        assertParseSuccess(parser, NAME_DESC_BOB + ID_DESC_BOB + GROUP_DESC_TUTORIAL + GROUP_DESC_RECITATION
                + TAG_DESC_FRIEND, new AddStudentCommand(expectedStudentMultipleGroups));

        //multiple similar groups - only one unique instance accepted
        Student expectedStudentWithMultipleSimilarGroups = new StudentBuilder(BOB)
                .withScores(new LinkedHashMap<>())
                .withGroups(VALID_GROUP_RECITATION)
                .withTags(VALID_TAG_FRIEND)
                .build();
        assertParseSuccess(parser, NAME_DESC_BOB + ID_DESC_BOB + GROUP_DESC_RECITATION
                + GROUP_DESC_RECITATION + TAG_DESC_FRIEND,
                new AddStudentCommand(expectedStudentWithMultipleSimilarGroups));

        // multiple tags - all accepted
        Student expectedStudentMultipleTags = new StudentBuilder(BOB)
                .withScores(new LinkedHashMap<>())
                .withGroups(VALID_GROUP_TUTORIAL)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND).build();
        assertParseSuccess(parser, NAME_DESC_BOB + ID_DESC_BOB + GROUP_DESC_TUTORIAL
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, new AddStudentCommand(expectedStudentMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Student expectedStudent = new StudentBuilder(AMY).withTags().withScores(new LinkedHashMap<>()).build();
        assertParseSuccess(parser, NAME_DESC_AMY + ID_DESC_AMY + GROUP_DESC_TUTORIAL,
                new AddStudentCommand(expectedStudent));

        // zero groups
        Student expectedStudentWithNoGroups = new StudentBuilder(AMY).withScores(new LinkedHashMap<>())
                .withGroups().build();
        assertParseSuccess(parser, NAME_DESC_AMY + ID_DESC_AMY + TAG_DESC_FRIEND,
                new AddStudentCommand(expectedStudentWithNoGroups));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddStudentCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + ID_DESC_BOB , expectedMessage);

        // missing ID prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_ID_BOB, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_ID_BOB, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + ID_DESC_BOB + GROUP_DESC_TUTORIAL
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid ID
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_ID_DESC + GROUP_DESC_TUTORIAL
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Id.MESSAGE_CONSTRAINTS);

        // invalid group
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + INVALID_GROUP_DESC
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Group.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + GROUP_DESC_TUTORIAL
                + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + INVALID_ID_DESC, Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + ID_DESC_BOB + GROUP_DESC_TUTORIAL
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddStudentCommand.MESSAGE_USAGE));
    }
}
