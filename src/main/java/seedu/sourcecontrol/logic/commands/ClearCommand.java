package seedu.sourcecontrol.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.SourceControl;

/**
 * Clears the Source Control application.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Source Control has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setSourceControl(new SourceControl());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
