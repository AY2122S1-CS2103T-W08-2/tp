package seedu.sourcecontrol.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.sourcecontrol.commons.util.CollectionUtil.equalsIgnoreOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.sourcecontrol.logic.commands.AddAllocCommand;
import seedu.sourcecontrol.logic.commands.AddAssessmentCommand;
import seedu.sourcecontrol.logic.commands.AddGroupCommand;
import seedu.sourcecontrol.logic.commands.AddScoreCommand;
import seedu.sourcecontrol.logic.commands.AddStudentCommand;
import seedu.sourcecontrol.logic.commands.AliasCommand;
import seedu.sourcecontrol.logic.commands.ClearCommand;
import seedu.sourcecontrol.logic.commands.Command;
import seedu.sourcecontrol.logic.commands.DeleteCommand;
import seedu.sourcecontrol.logic.commands.EditCommand;
import seedu.sourcecontrol.logic.commands.ExitCommand;
import seedu.sourcecontrol.logic.commands.ExportCommand;
import seedu.sourcecontrol.logic.commands.HelpCommand;
import seedu.sourcecontrol.logic.commands.ImportCommand;
import seedu.sourcecontrol.logic.commands.ListCommand;
import seedu.sourcecontrol.logic.commands.SearchCommand;
import seedu.sourcecontrol.logic.commands.ShowCommand;
import seedu.sourcecontrol.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class SourceControlParser {
    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private final List<Alias> aliases = new ArrayList<>();

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        requireNonNull(userInput);
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        String commandWord = matcher.group("commandWord");
        String arguments = matcher.group("arguments");

        // if it ends with a prefix, add a space so that the prefix can be picked up by the tokenizer
        if (arguments.matches(".* -[a-z]+$")) {
            arguments = arguments + " ";
        }

        switch (commandWord) {

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case SearchCommand.COMMAND_WORD:
            return new SearchCommandParser().parse(arguments);

        case ShowCommand.COMMAND_WORD:
            return new ShowCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case ImportCommand.COMMAND_WORD:
            return new ImportCommandParser().parse(arguments);

        case ExportCommand.COMMAND_WORD:
            return new ExportCommand();

        case AliasCommand.COMMAND_WORD:
            return new AliasCommandParser(this).parse(arguments);

        case AddGroupCommand.COMMAND_WORD:
            return new AddGroupCommandParser().parse(arguments);

        case AddAllocCommand.COMMAND_WORD:
            return new AddAllocCommandParser().parse(arguments);

        case AddAssessmentCommand.COMMAND_WORD:
            return new AddAssessmentCommandParser().parse(arguments);

        case AddStudentCommand.COMMAND_WORD:
            return new AddStudentCommandParser().parse(arguments);

        case AddScoreCommand.COMMAND_WORD:
            return new AddScoreCommandParser().parse(arguments);

        default:
            return parseAliases(userInput);
        }
    }

    /**
     * Adds or replaces the alias in the parser's list of aliases.
     */
    public void addAlias(Alias alias) {
        requireNonNull(alias);
        removeAlias(alias.getAliasWord());
        aliases.add(alias);
    }

    /**
     * Removes the provided alias from the parser's list of aliases.
     */
    public void removeAlias(String aliasWord) {
        requireNonNull(aliasWord);
        Optional<Alias> existing = getAlias(aliasWord);
        existing.ifPresent(aliases::remove);
    }

    /**
     * Returns an optional containing the alias with that alias word, or an empty optional if no alias matches.
     */
    public Optional<Alias> getAlias(String aliasWord) {
        return aliases.stream()
                .filter(a -> a.getAliasWord().equals(aliasWord))
                .findFirst();
    }

    /**
     * Tries to match the user input to an alias. Throws ParseException if none match.
     */
    private Command parseAliases(String userInput) throws ParseException {
        // matches the first word to the alias
        Optional<Alias> firstAlias = aliases.stream()
                .filter(alias -> userInput.split(" ")[0].equals(alias.getAliasWord()))
                .findFirst();

        Alias alias = firstAlias.orElseThrow(() -> new ParseException(MESSAGE_UNKNOWN_COMMAND));

        // replace the command of the user input, then try to parse again
        String newUserInput = alias.replaceFirst(userInput);
        return parseCommand(newUserInput);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SourceControlParser
                && equalsIgnoreOrder(aliases, ((SourceControlParser) other).aliases));
    }
}
