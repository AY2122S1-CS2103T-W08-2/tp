package seedu.sourcecontrol.logic.commands;

import seedu.sourcecontrol.model.Model;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting Source Control ...";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
    }

}
