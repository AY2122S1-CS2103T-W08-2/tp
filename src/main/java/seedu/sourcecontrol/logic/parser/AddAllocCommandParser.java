package seedu.sourcecontrol.logic.parser;

import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.sourcecontrol.logic.commands.AddAllocCommand;
import seedu.sourcecontrol.logic.commands.AddAllocCommand.AllocDescriptor;
import seedu.sourcecontrol.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddAllocCommand object
 */
public class AddAllocCommandParser implements Parser<AddAllocCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddAllocCommand
     * and returns an AddAllocCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddAllocCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_GROUP, PREFIX_NAME, PREFIX_ID);

        if (argMultimap.getValue(PREFIX_GROUP).isEmpty()
                || (argMultimap.getValue(PREFIX_NAME).isEmpty()
                && argMultimap.getValue(PREFIX_ID).isEmpty())
                || arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ID)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAllocCommand.MESSAGE_USAGE));
        }

        AddAllocCommand.AllocDescriptor allocDescriptor = new AllocDescriptor();
        allocDescriptor.setGroup(ParserUtil.parseGroup(argMultimap.getValue(PREFIX_GROUP).get()));
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            allocDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_ID).isPresent()) {
            allocDescriptor.setId(ParserUtil.parseID(argMultimap.getValue(PREFIX_ID).get()));
        }

        return new AddAllocCommand(allocDescriptor);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
