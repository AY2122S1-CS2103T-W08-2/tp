package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // TODO: handle each prefix separately, find command needs to take name, group, id predicate
        // remove any blank inputs
        String[] nameKeywords = trimmedArgs.split("\\s*-n\\s+");
        List<String> nameKeywordsList = new ArrayList<>();
        for (String keyword : nameKeywords) {
            if (!keyword.isEmpty()) {
                nameKeywordsList.add(keyword);
            }
        }

        return new FindCommand(new NameContainsKeywordsPredicate(nameKeywordsList));
    }

}
