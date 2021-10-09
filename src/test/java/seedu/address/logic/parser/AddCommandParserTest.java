package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.GROUP_DESC_RECITATION;
import static seedu.address.logic.commands.CommandTestUtil.GROUP_DESC_TUTORIAL;
import static seedu.address.logic.commands.CommandTestUtil.ID_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ID_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GROUP_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ID_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_RECITATION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_TUTORIAL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.student.Group;
import seedu.address.model.student.ID;
import seedu.address.model.student.Name;
import seedu.address.model.student.Student;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Student expectedStudent = new PersonBuilder(BOB)
                .withGroups(VALID_GROUP_TUTORIAL)
                .withScores(new HashMap<>())
                .withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + ID_DESC_BOB + GROUP_DESC_TUTORIAL
                + TAG_DESC_FRIEND, new AddCommand(expectedStudent));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + ID_DESC_BOB + GROUP_DESC_TUTORIAL
                + TAG_DESC_FRIEND, new AddCommand(expectedStudent));

        // multiple IDs - last ID accepted
        assertParseSuccess(parser, NAME_DESC_BOB + ID_DESC_AMY + ID_DESC_BOB + GROUP_DESC_TUTORIAL
                + TAG_DESC_FRIEND, new AddCommand(expectedStudent));

        // multiple groups - all accepted
        Student expectedStudentMultipleGroups = new PersonBuilder(BOB)
                .withScores(new HashMap<>())
                .withGroups(VALID_GROUP_TUTORIAL, VALID_GROUP_RECITATION)
                .withTags(VALID_TAG_FRIEND).build();
        assertParseSuccess(parser, NAME_DESC_BOB + ID_DESC_BOB + GROUP_DESC_TUTORIAL + GROUP_DESC_RECITATION
                + TAG_DESC_FRIEND, new AddCommand(expectedStudentMultipleGroups));

        // multiple tags - all accepted
        Student expectedStudentMultipleTags = new PersonBuilder(BOB)
                .withScores(new HashMap<>())
                .withGroups(VALID_GROUP_TUTORIAL)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND).build();
        assertParseSuccess(parser, NAME_DESC_BOB + ID_DESC_BOB + GROUP_DESC_TUTORIAL
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, new AddCommand(expectedStudentMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Student expectedStudent = new PersonBuilder(AMY).withTags().withScores(new HashMap<>()).build();
        assertParseSuccess(parser, NAME_DESC_AMY + ID_DESC_AMY + GROUP_DESC_TUTORIAL,
                new AddCommand(expectedStudent));

        // zero groups
        Student expectedStudentWithNoGroups = new PersonBuilder(AMY).withScores(new HashMap<>())
                .withGroups().build();
        assertParseSuccess(parser, NAME_DESC_AMY + ID_DESC_AMY + TAG_DESC_FRIEND,
                new AddCommand(expectedStudentWithNoGroups));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

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

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_ID_DESC + GROUP_DESC_TUTORIAL
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, ID.MESSAGE_CONSTRAINTS);

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
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
