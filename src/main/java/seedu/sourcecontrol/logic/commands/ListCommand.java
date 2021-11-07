package seedu.sourcecontrol.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import seedu.sourcecontrol.model.Model;

/**
 * Lists all students in the Source Control application to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all students. ";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
