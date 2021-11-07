package seedu.sourcecontrol.logic.parser;

import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ASSESSMENT_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ASSESSMENT_DESC_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ID_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ID_DESC_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_ASSESSMENT_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_ID_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.INVALID_SCORE_DESC;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.SCORE_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.SCORE_DESC_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ASSESSMENT_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ASSESSMENT_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_SCORE_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_SCORE_BOB;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.logic.commands.AddScoreCommand;
import seedu.sourcecontrol.logic.commands.AddScoreCommand.ScoreDescriptor;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.Score;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.testutil.ScoreDescriptorBuilder;

public class AddScoreCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddScoreCommand.MESSAGE_USAGE);

    private final AddScoreCommandParser parser = new AddScoreCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no assessment specified
        assertParseFailure(parser, NAME_DESC_AMY + SCORE_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, ID_DESC_AMY + SCORE_DESC_BOB, MESSAGE_INVALID_FORMAT);

        // no student identity specified
        assertParseFailure(parser, ASSESSMENT_DESC_AMY + SCORE_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // no score specified
        assertParseFailure(parser, ASSESSMENT_DESC_AMY + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, ASSESSMENT_DESC_AMY + ID_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // only assessment specified
        assertParseFailure(parser, ASSESSMENT_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // only student identity specified
        assertParseFailure(parser, NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, ID_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // only score specified
        assertParseFailure(parser, SCORE_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // no assessment and no student identity specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_redundantParts_failure() {
        // both name and id specified
        assertParseFailure(parser, ASSESSMENT_DESC_AMY
                + NAME_DESC_AMY + ID_DESC_AMY
                + SCORE_DESC_AMY, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // invalid arguments being parsed as preamble
        assertParseFailure(parser, ASSESSMENT_DESC_AMY
                + " some random string"
                + SCORE_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "some random string "
                + NAME_DESC_AMY
                + SCORE_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "some random string "
                + ID_DESC_AMY
                + SCORE_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, ASSESSMENT_DESC_AMY
                + NAME_DESC_AMY
                + "some random string ", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, ASSESSMENT_DESC_AMY
                + ID_DESC_AMY
                + "some random string ", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "-i " + VALID_ASSESSMENT_AMY
                + NAME_DESC_AMY
                + SCORE_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "-i " + VALID_ASSESSMENT_AMY
                + ID_DESC_AMY
                + SCORE_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, ASSESSMENT_DESC_AMY
                + "-i " + VALID_NAME_AMY
                + SCORE_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, ASSESSMENT_DESC_AMY
                + "-i " + VALID_ID_AMY
                + SCORE_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, ASSESSMENT_DESC_AMY
                + NAME_DESC_AMY
                + "-i " + VALID_SCORE_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, ASSESSMENT_DESC_AMY
                + ID_DESC_AMY + "-i "
                + VALID_SCORE_AMY, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid assessment value
        assertParseFailure(parser, INVALID_ASSESSMENT_DESC
                + NAME_DESC_AMY
                + SCORE_DESC_AMY, Assessment.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, INVALID_ASSESSMENT_DESC
                + ID_DESC_AMY
                + SCORE_DESC_AMY, Assessment.MESSAGE_CONSTRAINTS);

        // invalid student identity value
        assertParseFailure(parser, ASSESSMENT_DESC_AMY
                + INVALID_NAME_DESC
                + SCORE_DESC_AMY, Name.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, ASSESSMENT_DESC_AMY
                + INVALID_ID_DESC
                + SCORE_DESC_AMY, Id.MESSAGE_CONSTRAINTS);

        // invalid score value
        assertParseFailure(parser, ASSESSMENT_DESC_AMY
                + NAME_DESC_AMY
                + INVALID_SCORE_DESC, Score.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, ASSESSMENT_DESC_AMY
                + ID_DESC_AMY
                + INVALID_SCORE_DESC, Score.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, ASSESSMENT_DESC_AMY
                + INVALID_ID_DESC
                + INVALID_SCORE_DESC, Id.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validFields_success() {
        // add score by name
        String userInput = ASSESSMENT_DESC_AMY + NAME_DESC_AMY + SCORE_DESC_AMY;
        ScoreDescriptor descriptor = new ScoreDescriptorBuilder()
                .withAssessment(VALID_ASSESSMENT_AMY)
                .withName(VALID_NAME_AMY)
                .withScore(VALID_SCORE_AMY).build();
        AddScoreCommand expectedCommand = new AddScoreCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);

        // add score by id
        userInput = ASSESSMENT_DESC_AMY + ID_DESC_AMY + SCORE_DESC_AMY;
        descriptor = new ScoreDescriptorBuilder()
                .withAssessment(VALID_ASSESSMENT_AMY)
                .withId(VALID_ID_AMY)
                .withScore(VALID_SCORE_AMY).build();
        expectedCommand = new AddScoreCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedSimilarFields_acceptsLast() {
        // add score by name
        String userInput = ASSESSMENT_DESC_AMY + NAME_DESC_AMY + SCORE_DESC_AMY
                + ASSESSMENT_DESC_BOB + NAME_DESC_AMY + SCORE_DESC_AMY
                + ASSESSMENT_DESC_BOB + NAME_DESC_BOB + SCORE_DESC_BOB;
        ScoreDescriptor descriptor = new ScoreDescriptorBuilder()
                .withAssessment(VALID_ASSESSMENT_BOB)
                .withName(VALID_NAME_BOB)
                .withScore(VALID_SCORE_BOB).build();
        AddScoreCommand expectedCommand = new AddScoreCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);

        // add score by id
        userInput = ASSESSMENT_DESC_AMY + ID_DESC_AMY + SCORE_DESC_AMY
                + ASSESSMENT_DESC_BOB + ID_DESC_AMY + SCORE_DESC_AMY
                + ASSESSMENT_DESC_BOB + ID_DESC_BOB + SCORE_DESC_BOB;
        descriptor = new ScoreDescriptorBuilder()
                .withAssessment(VALID_ASSESSMENT_BOB)
                .withId(VALID_ID_BOB)
                .withScore(VALID_SCORE_BOB).build();
        expectedCommand = new AddScoreCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedDifferentFields_failure() {
        // add score by name
        String userInput = ASSESSMENT_DESC_AMY + ID_DESC_AMY + SCORE_DESC_AMY
                + ASSESSMENT_DESC_BOB + NAME_DESC_BOB + SCORE_DESC_BOB;
        assertParseFailure(parser, userInput, MESSAGE_INVALID_FORMAT);

        // add score by id
        userInput = ASSESSMENT_DESC_AMY + NAME_DESC_AMY + SCORE_DESC_AMY
                + ASSESSMENT_DESC_BOB + ID_DESC_BOB + SCORE_DESC_BOB;
        assertParseFailure(parser, userInput, MESSAGE_INVALID_FORMAT);
    }
}
