package seedu.sourcecontrol.logic.parser;

import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_COMMAND;

import java.util.Optional;

import seedu.sourcecontrol.logic.commands.AliasCommand;
import seedu.sourcecontrol.logic.parser.exceptions.ParseException;

public class AliasCommandParser implements Parser<AliasCommand> {
    private final SourceControlParser parser;

    public AliasCommandParser(SourceControlParser parser) {
        this.parser = parser;
    }

    @Override
    public AliasCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ALIAS, PREFIX_COMMAND);

        if (argMultimap.getValue(PREFIX_ALIAS).isEmpty()
            || argMultimap.getValue(PREFIX_COMMAND).isEmpty()) {
            throw new ParseException(AliasCommand.MESSAGE_USAGE);
        }

        String aliasWord = argMultimap.getValue(PREFIX_ALIAS).get().strip();
        String commandWord = argMultimap.getValue(PREFIX_COMMAND).get().strip();

        checkAliasWord(aliasWord, parser);
        checkCommandWord(commandWord, parser);

        // short circuit if removing existing alias
        if (aliasWord.equals(commandWord)) {
            Alias newAlias = new Alias(aliasWord, commandWord);
            return new AliasCommand(newAlias, parser);
        }

        commandWord = flattenCommandWord(commandWord, parser);

        Alias newAlias = new Alias(aliasWord, commandWord);
        return new AliasCommand(newAlias, parser);
    }

    /**
     * Checks if the provided commandWord is a single word, and that the parser recognises it as a command word.
     */
    private static boolean isValidCommandWord(String commandWord, SourceControlParser parser) {
        if (commandWord.isEmpty() || commandWord.contains(" ")) {
            return false;
        }

        try {
            parser.parseCommand(commandWord);
        } catch (ParseException e) {
            if (e.getMessage().equals(MESSAGE_UNKNOWN_COMMAND)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Verifies if the alias word can make a valid alias for the provided parser.
     * Checks that the alias is a single alphanumeric word, and that it is not a default command.
     */
    public static void checkAliasWord(String aliasWord, SourceControlParser parser) throws ParseException {
        if (!Alias.isValidAlias(aliasWord)) {
            throw new ParseException(Alias.MESSAGE_CONSTRAINTS);
        }

        // prevents trying to alias a default command
        if (isValidCommandWord(aliasWord, parser) && parser.getAlias(aliasWord).isEmpty()) {
            throw new ParseException(AliasCommand.MESSAGE_OVERWRITE_DEFAULT);
        }
    }

    /**
     * Verifies if the command word can be parsed by the
     */
    public static void checkCommandWord(String commandWord, SourceControlParser parser) throws ParseException {
        if (!isValidCommandWord(commandWord, parser)) {
            throw new ParseException(AliasCommand.MESSAGE_UNKNOWN_OLD_COMMAND);
        }
    }

    /**
     * Prevents the chaining of aliases.
     * If there is an existing alias "bye" for "exit", and "bye" is the command word, it will be flattened to "exit".
     */
    public static String flattenCommandWord(String commandWord, SourceControlParser parser) {
        Optional<Alias> existingAlias = parser.getAlias(commandWord);
        return existingAlias.map(Alias::getCommandWord).orElse(commandWord);
    }
}
