package seedu.sourcecontrol.logic.parser;

import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.stream.Stream;

import seedu.sourcecontrol.commons.core.index.Index;
import seedu.sourcecontrol.logic.commands.DeleteCommand;
import seedu.sourcecontrol.logic.parser.exceptions.ParseException;


/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        Prefix[] prefixes = new Prefix[]{
            CliSyntax.PREFIX_NAME, CliSyntax.PREFIX_ID, CliSyntax.PREFIX_GROUP,
            CliSyntax.PREFIX_ASSESSMENT, CliSyntax.PREFIX_SCORE, CliSyntax.PREFIX_TAG,
            CliSyntax.PREFIX_FILE, CliSyntax.PREFIX_ALIAS, CliSyntax.PREFIX_COMMAND};

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, prefixes);

        if (argMultimap.getPreamble().isEmpty() || !isNoPrefixPresent(argMultimap, prefixes)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(ParserUtil.MESSAGE_INVALID_INDEX);
        }
    }

    /**
     * Returns true if none of the prefixes present in the given {@code ArgumentMultimap}.
     */
    private static boolean isNoPrefixPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isEmpty());
    }

}
