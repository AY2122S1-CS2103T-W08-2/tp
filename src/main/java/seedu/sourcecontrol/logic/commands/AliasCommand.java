package seedu.sourcecontrol.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_COMMAND;

import seedu.sourcecontrol.logic.parser.Alias;
import seedu.sourcecontrol.logic.parser.SourceControlParser;
import seedu.sourcecontrol.model.Model;

/**
 * Adds an alias to the SourceControlParser and UserPrefs.
 */
public class AliasCommand extends Command {

    public static final String COMMAND_WORD = "alias";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an alias for a command. \n"
            + "Parameters: "
            + PREFIX_COMMAND + "<existing_command> " + PREFIX_ALIAS + "<alias>\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_COMMAND + AddStudentCommand.COMMAND_WORD + " "
            + PREFIX_ALIAS + "addstu";

    public static final String MESSAGE_ADD_SUCCESS = "New alias added: %1$s";
    public static final String MESSAGE_REMOVE_SUCCESS = "Alias removed: %1$s";
    public static final String MESSAGE_UNKNOWN_OLD_COMMAND = "Specified command to be aliased does not exist. ";
    public static final String MESSAGE_OVERWRITE_DEFAULT = "You can't overwrite a default command. ";

    private final Alias alias;
    private final SourceControlParser parser;

    /**
     * Creates an AliasCommand to add the specified {@code Alias}
     */
    public AliasCommand(Alias alias, SourceControlParser parser) {
        requireAllNonNull(alias, parser);
        this.alias = alias;
        this.parser = parser;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (alias.isRedundant()) {
            model.removeAlias(alias.getAliasWord());
            parser.removeAlias(alias.getAliasWord());
            return new CommandResult(String.format(MESSAGE_REMOVE_SUCCESS, alias.getAliasWord()));
        } else {
            model.addAlias(alias);
            parser.addAlias(alias);
            return new CommandResult(String.format(MESSAGE_ADD_SUCCESS, alias.getAliasWord()));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AliasCommand // instanceof handles nulls
                && alias.equals(((AliasCommand) other).alias));
    }
}
