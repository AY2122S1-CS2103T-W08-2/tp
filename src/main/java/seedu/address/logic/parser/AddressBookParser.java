package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        boolean isTwoWordCommand = false;
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        String commandWord = matcher.group("commandWord");
        String arguments = matcher.group("arguments");
        if ((arguments.length() != 0) && (!arguments.startsWith(" -"))) {
            isTwoWordCommand = true;
            if (Character.isDigit(arguments.charAt(1))) {
                isTwoWordCommand = false;
            }
        }
        if (isTwoWordCommand) {
            commandWord = convertFormat(commandWord, arguments);
            arguments = convertFormatArguments(arguments);
        }
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Removes second word of command from arguments.
     *
     * @param arguments arguments to format.
     * @return formatted arguments.
     */
    private String convertFormatArguments(String arguments) {
        int index = 0;
        if (arguments.contains("-")) {
            index = arguments.indexOf("-");
        }
        if (index == 0) {
            return null;
        } else {
            String argumentsFormatted = arguments.substring(index - 1);
            return argumentsFormatted;
        }
    }

    /**
     * Converts commandWord to the two word format and retrieves second word from arguments.
     *
     * @param commandWord commandWord to format.
     * @param arguments arguments to retrieve second half of commandWord from.
     * @return formatted commandWord.
     */
    private String convertFormat(String commandWord, String arguments) {
        int index = 0;
        if (arguments.contains("-")) {
            index = arguments.indexOf("-");
        }
        if (index == 0) {
            return commandWord + arguments;
        } else {
            String commandWordPartTwo = commandWord + arguments.substring(0, index - 1);
            return commandWordPartTwo;
        }
    }

}
