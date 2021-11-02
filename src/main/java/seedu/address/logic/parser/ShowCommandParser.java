package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSESSMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.nio.file.Path;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ShowCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ShowCommand object
 */
public class ShowCommandParser implements Parser<ShowCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ShowCommand
     * and returns an ShowCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ShowCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ID, PREFIX_ASSESSMENT, PREFIX_GROUP, PREFIX_FILE);

        Path savePath = null;
        if (argMultimap.getValue(PREFIX_FILE).isPresent()) {
            savePath = ParserUtil.parsePath(argMultimap.getValue(PREFIX_FILE).get(), ".png");
        }

        return argMultimap.getPreamble().isEmpty()
                ? parseByPrefixes(argMultimap, savePath)
                : parseByIndex(argMultimap, savePath);
    }

    /**
     * Handles parsing by {@code Index}.
     */
    public ShowCommand parseByIndex(ArgumentMultimap argMultimap, Path savePath) throws ParseException {
        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        return new ShowCommand(index, savePath);
    }

    /**
     * Handles parsing by prefixes.
     */
    public ShowCommand parseByPrefixes(ArgumentMultimap argMultimap, Path savePath) throws ParseException {
        if (isInvalidPrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ID, PREFIX_ASSESSMENT, PREFIX_GROUP)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowCommand.MESSAGE_USAGE));
        }

        return argMultimap.getValue(PREFIX_NAME).isPresent()
                ? new ShowCommand(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()), savePath)
                : argMultimap.getValue(PREFIX_ID).isPresent()
                ? new ShowCommand(ParserUtil.parseID(argMultimap.getValue(PREFIX_ID).get()), savePath)
                : argMultimap.getValue(PREFIX_ASSESSMENT).isPresent()
                ? new ShowCommand(ParserUtil.parseAssessment(argMultimap.getValue(PREFIX_ASSESSMENT).get()), savePath)
                : new ShowCommand(ParserUtil.parseGroup(argMultimap.getValue(PREFIX_GROUP).get()), savePath);
    }

    /**
     * Returns true if only one of the prefixes is present in the given {@code ArgumentMultimap},
     * i.e. false if there are multiple prefixes or no prefix present.
     */
    private static boolean isInvalidPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).filter(prefix -> argumentMultimap.getValue(prefix).isPresent()).count() != 1;
    }

}
