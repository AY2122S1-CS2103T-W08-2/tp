package seedu.sourcecontrol.logic.parser;

import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.sourcecontrol.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.logic.commands.DeleteCommand;

public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validIndex_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new DeleteCommand(INDEX_FIRST_STUDENT));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        // non-integer index
        assertParseFailure(parser, "a", ParserUtil.MESSAGE_INVALID_INDEX);

        // negative index
        assertParseFailure(parser, "-1", ParserUtil.MESSAGE_INVALID_INDEX);

        // zero index
        assertParseFailure(parser, "0", ParserUtil.MESSAGE_INVALID_INDEX);

        // exceeding MAX_INT index
        assertParseFailure(parser, "2147483648", ParserUtil.MESSAGE_INVALID_INDEX);
    }
}
