package seedu.sourcecontrol.logic.parser;

import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import seedu.sourcecontrol.logic.commands.AddAllocCommand.AllocDescriptor;
import seedu.sourcecontrol.logic.commands.AddGroupCommand;
import seedu.sourcecontrol.logic.parser.exceptions.ParseException;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;

/**
 * Parses input arguments and creates a new AddGroupCommand object
 */
public class AddGroupCommandParser implements Parser<AddGroupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddGroupCommand
     * and returns an AddGroupCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddGroupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_GROUP, PREFIX_NAME, PREFIX_ID);

        if (!arePrefixesPresent(argMultimap, PREFIX_GROUP) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddGroupCommand.MESSAGE_USAGE));
        }

        Group group = ParserUtil.parseGroup(argMultimap.getValue(PREFIX_GROUP).get());

        List<AllocDescriptor> allocDescriptors = new ArrayList<>();

        List<Name> names = ParserUtil.parseNames(argMultimap.getAllValues(PREFIX_NAME));
        for (Name name : names) {
            AllocDescriptor allocDescriptor = new AllocDescriptor();
            allocDescriptor.setGroup(group);
            allocDescriptor.setName(name);
            allocDescriptors.add(allocDescriptor);
        }

        List<Id> ids = ParserUtil.parseIds(argMultimap.getAllValues(PREFIX_ID));
        for (Id id : ids) {
            AllocDescriptor allocDescriptor = new AllocDescriptor();
            allocDescriptor.setGroup(group);
            allocDescriptor.setId(id);
            allocDescriptors.add(allocDescriptor);
        }

        return new AddGroupCommand(group, allocDescriptors);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
