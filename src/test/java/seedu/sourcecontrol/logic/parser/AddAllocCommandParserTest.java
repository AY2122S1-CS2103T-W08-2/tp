package seedu.sourcecontrol.logic.parser;

import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.GROUP_DESC_RECITATION;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.GROUP_DESC_TUTORIAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ID_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ID_DESC_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_GROUP_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_ID_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_RECITATION;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_TUTORIAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.logic.commands.AddAllocCommand;
import seedu.sourcecontrol.logic.commands.AddAllocCommand.AllocDescriptor;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.testutil.AllocDescriptorBuilder;

public class AddAllocCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAllocCommand.MESSAGE_USAGE);

    private final AddAllocCommandParser parser = new AddAllocCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no group specified
        assertParseFailure(parser, NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, ID_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // no student identity specified
        assertParseFailure(parser, GROUP_DESC_TUTORIAL, MESSAGE_INVALID_FORMAT);

        // no group and no student identity specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_redundantParts_failure() {
        // both name and id specified
        assertParseFailure(parser, GROUP_DESC_TUTORIAL + NAME_DESC_AMY + ID_DESC_AMY, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // invalid arguments being parsed as preamble
        assertParseFailure(parser, GROUP_DESC_TUTORIAL + " some random string", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "some random string " + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "some random string " + ID_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "-i " + VALID_GROUP_TUTORIAL + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "-i " + VALID_GROUP_TUTORIAL + ID_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, GROUP_DESC_TUTORIAL + "-i " + VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, GROUP_DESC_TUTORIAL + "-i " + VALID_ID_AMY, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid group value
        assertParseFailure(parser, INVALID_GROUP_DESC + NAME_DESC_AMY, Group.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, INVALID_GROUP_DESC + ID_DESC_AMY, Group.MESSAGE_CONSTRAINTS);

        // invalid student identity value
        assertParseFailure(parser, GROUP_DESC_TUTORIAL + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, GROUP_DESC_TUTORIAL + INVALID_ID_DESC, Id.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, INVALID_GROUP_DESC + INVALID_ID_DESC, Group.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validFields_success() {
        // allocate by name
        String userInput = GROUP_DESC_TUTORIAL + NAME_DESC_AMY;
        AllocDescriptor descriptor = new AllocDescriptorBuilder()
                .withGroup(VALID_GROUP_TUTORIAL)
                .withName(VALID_NAME_AMY).build();
        AddAllocCommand expectedCommand = new AddAllocCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);

        // allocate by id
        userInput = GROUP_DESC_TUTORIAL + ID_DESC_AMY;
        descriptor = new AllocDescriptorBuilder()
                .withGroup(VALID_GROUP_TUTORIAL)
                .withId(VALID_ID_AMY).build();
        expectedCommand = new AddAllocCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedSimilarFields_acceptsLast() {
        // allocate by name
        String userInput = GROUP_DESC_TUTORIAL + NAME_DESC_AMY
                + GROUP_DESC_RECITATION + NAME_DESC_AMY
                + GROUP_DESC_RECITATION + NAME_DESC_BOB;
        AllocDescriptor descriptor = new AllocDescriptorBuilder()
                .withGroup(VALID_GROUP_RECITATION)
                .withName(VALID_NAME_BOB).build();
        AddAllocCommand expectedCommand = new AddAllocCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);

        // allocate by id
        userInput = GROUP_DESC_TUTORIAL + ID_DESC_AMY
                + GROUP_DESC_RECITATION + ID_DESC_AMY
                + GROUP_DESC_RECITATION + ID_DESC_BOB;
        descriptor = new AllocDescriptorBuilder()
                .withGroup(VALID_GROUP_RECITATION)
                .withId(VALID_ID_BOB).build();
        expectedCommand = new AddAllocCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedDifferentFields_failure() {
        // allocate by name
        String userInput = GROUP_DESC_TUTORIAL + ID_DESC_AMY
                + GROUP_DESC_RECITATION + NAME_DESC_BOB;
        assertParseFailure(parser, userInput, MESSAGE_INVALID_FORMAT);

        // allocate by id
        userInput = GROUP_DESC_TUTORIAL + NAME_DESC_AMY
                + GROUP_DESC_RECITATION + ID_DESC_BOB;
        assertParseFailure(parser, userInput, MESSAGE_INVALID_FORMAT);
    }
}
