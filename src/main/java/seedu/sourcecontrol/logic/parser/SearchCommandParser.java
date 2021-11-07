package seedu.sourcecontrol.logic.parser;

import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;

import seedu.sourcecontrol.logic.commands.SearchCommand;
import seedu.sourcecontrol.logic.parser.exceptions.ParseException;
import seedu.sourcecontrol.model.student.group.GroupContainsKeywordsPredicate;
import seedu.sourcecontrol.model.student.id.IdContainsKeywordsPredicate;
import seedu.sourcecontrol.model.student.name.NameContainsKeywordsPredicate;
import seedu.sourcecontrol.model.student.tag.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new SearchCommand object
 */
public class SearchCommandParser implements Parser<SearchCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SearchCommand
     * and returns a SearchCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SearchCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ID, PREFIX_GROUP, PREFIX_TAG);

        // catch case of empty input
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        }

        int count = 0;
        String searchType = "";
        String prefix = "";

        // identify search input type
        if (!argMultimap.getValue(PREFIX_NAME).isEmpty()) {
            searchType = "NAME";
            prefix = " -n ";
            count++;
        }

        if (!argMultimap.getValue(PREFIX_ID).isEmpty()) {
            searchType = "ID";
            prefix = " -i ";
            count++;
        }

        if (!argMultimap.getValue(PREFIX_GROUP).isEmpty()) {
            searchType = "GROUP";
            prefix = " -g ";
            count++;
        }

        if (!argMultimap.getValue(PREFIX_TAG).isEmpty()) {
            searchType = "TAG";
            prefix = " -t ";
            count++;
        }

        // catch case of more than one search input type
        if (count != 1) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        }

        int prefixIndex = args.indexOf(prefix, 0);
        String searchTerm = args.substring(prefixIndex + 4).trim();

        // separate all search inputs by spaces
        String[] keywords = searchTerm.split("\\s+");
        List<String> keywordsList = new ArrayList<>();
        for (String keyword : keywords) {
            if (!keyword.isEmpty()) {
                keywordsList.add(keyword);
            }
        }

        // execute according to search input type
        switch(searchType) {
        case "NAME":
            return new SearchCommand(new NameContainsKeywordsPredicate(keywordsList));

        case "ID":
            return new SearchCommand(new IdContainsKeywordsPredicate(keywordsList));

        case "GROUP":
            return new SearchCommand(new GroupContainsKeywordsPredicate(keywordsList));

        case "TAG":
            return new SearchCommand(new TagContainsKeywordsPredicate(keywordsList));

        default:
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        }
    }
}
