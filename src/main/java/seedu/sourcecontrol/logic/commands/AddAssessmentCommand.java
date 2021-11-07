package seedu.sourcecontrol.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ASSESSMENT;

import seedu.sourcecontrol.logic.commands.exceptions.CommandException;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.student.assessment.Assessment;

/**
 * Adds a group to the Source Control application.
 */
public class AddAssessmentCommand extends Command {

    public static final String COMMAND_WORD = "addassessment";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an assessment to the database. \n"
            + "Parameters: "
            + PREFIX_ASSESSMENT + "<assessment_name>\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ASSESSMENT + "P01";

    public static final String MESSAGE_SUCCESS = "New assessment added: %1$s";
    public static final String MESSAGE_DUPLICATE_ASSESSMENT = "This assessment already exists in the database. ";

    private final Assessment assessment;

    /**
     * Creates an AddAssessmentCommand to add the specified {@code Assessment}
     */
    public AddAssessmentCommand(Assessment assessment) {
        requireNonNull(assessment);
        this.assessment = assessment;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasAssessment(assessment)) {
            throw new CommandException(MESSAGE_DUPLICATE_ASSESSMENT);
        }
        model.addAssessment(assessment);
        return new CommandResult(String.format(MESSAGE_SUCCESS, assessment));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddAssessmentCommand // instanceof handles nulls
                && assessment.equals(((AddAssessmentCommand) other).assessment));
    }
}
