package seedu.sourcecontrol.logic.parser;

import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_COMMAND;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.sourcecontrol.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.logic.commands.AliasCommand;
import seedu.sourcecontrol.logic.commands.ClearCommand;
import seedu.sourcecontrol.logic.commands.ExitCommand;

public class AliasCommandParserTest {
    private final SourceControlParser abParser = new SourceControlParser();
    private final AliasCommandParser parser = new AliasCommandParser(abParser);
    private final String validAliasWord = "aliasWord";
    private final String validCommandWord = ClearCommand.COMMAND_WORD;
    private final String validCommandWord2 = ExitCommand.COMMAND_WORD;
    private final String invalidAliasWord = validCommandWord + " hello";
    private final String invalidCommandWord = "invalidCommandWord";

    @Test
    public void parse_missingPrefix_failure() {
        assertParseFailure(parser, validAliasWord + " " + validCommandWord, AliasCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_missingAliasWord_failure() {
        assertParseFailure(parser, " " + PREFIX_COMMAND + validCommandWord, AliasCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_missingCommandWord_failure() {
        assertParseFailure(parser, " " + PREFIX_ALIAS + validAliasWord, AliasCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_unknownCommandWord_failure() {
        assertParseFailure(parser, " " + PREFIX_ALIAS + validAliasWord + " "
                + PREFIX_COMMAND + invalidCommandWord,
                AliasCommand.MESSAGE_UNKNOWN_OLD_COMMAND);

        // extra word on command word
        assertParseFailure(parser, " " + PREFIX_ALIAS + validAliasWord + " "
                + PREFIX_COMMAND + validCommandWord + " hello",
                AliasCommand.MESSAGE_UNKNOWN_OLD_COMMAND);

        // extra characters on command word
        assertParseFailure(parser, " " + PREFIX_ALIAS + validAliasWord + " "
                + PREFIX_COMMAND + validCommandWord + "hello",
                AliasCommand.MESSAGE_UNKNOWN_OLD_COMMAND);
    }

    @Test
    public void parse_invalidAliasWord_failure() {
        // multiple words alias
        assertParseFailure(parser, " " + PREFIX_ALIAS + invalidAliasWord + " "
                + PREFIX_COMMAND + validCommandWord,
                Alias.MESSAGE_CONSTRAINTS);

        // empty alias
        assertParseFailure(parser, " " + PREFIX_ALIAS + PREFIX_COMMAND + validCommandWord,
                Alias.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_overwriteDefault_failure() {
        assertParseFailure(parser, " " + PREFIX_ALIAS + validCommandWord + " "
                + PREFIX_COMMAND + validCommandWord2,
                AliasCommand.MESSAGE_OVERWRITE_DEFAULT);
    }

    @Test
    public void parse_validInputs_success() {
        AliasCommand expectedCommand = new AliasCommand(new Alias(validAliasWord, validCommandWord), abParser);

        // alias prefix first
        assertParseSuccess(parser, " " + PREFIX_ALIAS + validAliasWord + " "
                + PREFIX_COMMAND + validCommandWord,
                expectedCommand);

        // command prefix first
        assertParseSuccess(parser, " " + PREFIX_COMMAND + validCommandWord + " "
                + PREFIX_ALIAS + validAliasWord,
                expectedCommand);
    }
}
