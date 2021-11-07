package seedu.sourcecontrol.logic.parser;

import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.GROUP_DESC_TUTORIAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ID_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ID_DESC_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_GROUP_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_ID_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_TUTORIAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.logic.commands.AddAllocCommand.AllocDescriptor;
import seedu.sourcecontrol.logic.commands.AddGroupCommand;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.testutil.AllocDescriptorBuilder;
import seedu.sourcecontrol.testutil.GroupBuilder;

public class AddGroupCommandParserTest {
    private AddGroupCommandParser parser = new AddGroupCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        // with whitespace
        Group expectedEmptyGroup = new GroupBuilder().withName(VALID_GROUP_TUTORIAL).build();
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + GROUP_DESC_TUTORIAL,
                new AddGroupCommand(expectedEmptyGroup, new ArrayList<>()));

        // 1 name and 1 ID - both accepted
        Group expectedGroup = new GroupBuilder().withName(VALID_GROUP_TUTORIAL).build();
        AllocDescriptor descriptorWithAmyId = new AllocDescriptorBuilder()
                .withGroup(VALID_GROUP_TUTORIAL)
                .withId(VALID_ID_AMY).build();
        AllocDescriptor descriptorWithBobName = new AllocDescriptorBuilder()
                .withGroup(VALID_GROUP_TUTORIAL)
                .withName(VALID_NAME_BOB).build();
        List<AllocDescriptor> expectedMixedDescriptors = Arrays.asList(descriptorWithAmyId, descriptorWithBobName);
        assertParseSuccess(parser, GROUP_DESC_TUTORIAL + ID_DESC_AMY + NAME_DESC_BOB,
                new AddGroupCommand(expectedGroup, expectedMixedDescriptors));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // without any name or ID
        Group expectedEmptyGroup = new GroupBuilder().withName(VALID_GROUP_TUTORIAL).build();
        assertParseSuccess(parser, GROUP_DESC_TUTORIAL, new AddGroupCommand(expectedEmptyGroup, new ArrayList<>()));

        // only names - all accepted
        Group expectedGroupWithStudents = new GroupBuilder().withName(VALID_GROUP_TUTORIAL).build();
        AllocDescriptor descriptorWithAmyName = new AllocDescriptorBuilder()
                .withGroup(VALID_GROUP_TUTORIAL)
                .withName(VALID_NAME_AMY).build();
        AllocDescriptor descriptorWithBobName = new AllocDescriptorBuilder()
                .withGroup(VALID_GROUP_TUTORIAL)
                .withName(VALID_NAME_BOB).build();
        List<AllocDescriptor> expectedNameDescriptors = Arrays.asList(descriptorWithAmyName, descriptorWithBobName);
        assertParseSuccess(parser, GROUP_DESC_TUTORIAL + NAME_DESC_AMY + NAME_DESC_BOB,
                new AddGroupCommand(expectedGroupWithStudents, expectedNameDescriptors));

        // only IDs - all accepted
        AllocDescriptor descriptorWithAmyId = new AllocDescriptorBuilder()
                .withGroup(VALID_GROUP_TUTORIAL)
                .withId(VALID_ID_AMY).build();
        AllocDescriptor descriptorWithBobId = new AllocDescriptorBuilder()
                .withGroup(VALID_GROUP_TUTORIAL)
                .withId(VALID_ID_BOB).build();
        List<AllocDescriptor> expectedIdDescriptors = Arrays.asList(descriptorWithAmyId, descriptorWithBobId);
        assertParseSuccess(parser, GROUP_DESC_TUTORIAL + ID_DESC_AMY + ID_DESC_BOB,
                new AddGroupCommand(expectedGroupWithStudents, expectedIdDescriptors));
    }

    @Test
    public void parse_groupFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddGroupCommand.MESSAGE_USAGE);

        // missing group prefix
        assertParseFailure(parser, VALID_GROUP_TUTORIAL, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid group name
        assertParseFailure(parser, INVALID_GROUP_DESC, Group.MESSAGE_CONSTRAINTS);

        // invalid ID
        assertParseFailure(parser, GROUP_DESC_TUTORIAL + INVALID_ID_DESC, Id.MESSAGE_CONSTRAINTS);
    }
}
