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
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_RECITATION;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_TUTORIAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.sourcecontrol.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.sourcecontrol.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.sourcecontrol.testutil.TypicalIndexes.INDEX_THIRD_STUDENT;
import static seedu.sourcecontrol.testutil.TypicalStudents.getTypicalSourceControl;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.commons.core.index.Index;
import seedu.sourcecontrol.logic.commands.EditCommand;
import seedu.sourcecontrol.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ModelManager;
import seedu.sourcecontrol.model.UserPrefs;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.model.student.tag.Tag;
import seedu.sourcecontrol.testutil.EditStudentDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private final EditCommandParser parser = new EditCommandParser();

    private Model model = new ModelManager(getTypicalSourceControl(), new UserPrefs());

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, ParserUtil.MESSAGE_INVALID_INDEX);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, ParserUtil.MESSAGE_INVALID_INDEX);

        // exceeding MAX_INT index
        assertParseFailure(parser, "2147483648", ParserUtil.MESSAGE_INVALID_INDEX);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", ParserUtil.MESSAGE_INVALID_INDEX);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_ID_DESC, Id.MESSAGE_CONSTRAINTS); // invalid ID
        assertParseFailure(parser, "1" + INVALID_GROUP_DESC, Group.MESSAGE_CONSTRAINTS); // invalid group
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag

        // invalid ID followed by valid group
        assertParseFailure(parser, "1" + INVALID_ID_DESC + GROUP_DESC_TUTORIAL, Id.MESSAGE_CONSTRAINTS);

        // valid ID followed by invalid ID. The test case for invalid phone followed by valid phone
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + ID_DESC_BOB + INVALID_ID_DESC, Id.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Student} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_EMPTY + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_ID_DESC + VALID_GROUP_TUTORIAL,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_STUDENT;
        String userInput = targetIndex.getOneBased() + ID_DESC_BOB + TAG_DESC_HUSBAND
                + GROUP_DESC_TUTORIAL + NAME_DESC_AMY + GROUP_DESC_RECITATION + TAG_DESC_FRIEND;

        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withName(VALID_NAME_AMY)
                .withId(VALID_ID_BOB).withGroups(VALID_GROUP_TUTORIAL, VALID_GROUP_RECITATION)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_STUDENT;
        String userInput = targetIndex.getOneBased() + ID_DESC_BOB + GROUP_DESC_TUTORIAL;

        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withId(VALID_ID_BOB)
                .withGroups(VALID_GROUP_TUTORIAL).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_STUDENT;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // ID
        userInput = targetIndex.getOneBased() + ID_DESC_AMY;
        descriptor = new EditStudentDescriptorBuilder().withId(VALID_ID_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // groups
        userInput = targetIndex.getOneBased() + GROUP_DESC_TUTORIAL;
        descriptor = new EditStudentDescriptorBuilder().withGroups(VALID_GROUP_TUTORIAL).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_FRIEND;
        descriptor = new EditStudentDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_STUDENT;
        String userInput = targetIndex.getOneBased() + ID_DESC_AMY + GROUP_DESC_TUTORIAL + TAG_DESC_FRIEND
                + ID_DESC_AMY + GROUP_DESC_TUTORIAL + TAG_DESC_FRIEND
                + ID_DESC_BOB + GROUP_DESC_RECITATION + TAG_DESC_HUSBAND;

        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withId(VALID_ID_BOB)
                .withGroups(VALID_GROUP_TUTORIAL, VALID_GROUP_RECITATION)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_STUDENT;
        String userInput = targetIndex.getOneBased() + INVALID_ID_DESC + ID_DESC_BOB;
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withId(VALID_ID_BOB).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + GROUP_DESC_TUTORIAL + INVALID_ID_DESC + ID_DESC_BOB;
        descriptor = new EditStudentDescriptorBuilder().withId(VALID_ID_BOB).withGroups(VALID_GROUP_TUTORIAL).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_STUDENT;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
