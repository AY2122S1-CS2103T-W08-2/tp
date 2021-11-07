package seedu.sourcecontrol.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.sourcecontrol.logic.commands.exceptions.CommandException;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.student.Student;

/**
 * Adds a student to the Source Control application.
 */
public class AddStudentCommand extends Command {

    public static final String COMMAND_WORD = "addstudent";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a student to the database. \n"
            + "Parameters: "
            + PREFIX_NAME + "<student_name> "
            + PREFIX_ID + "<student_id> "
            + "[" + PREFIX_GROUP + "<group_name>]... "
            + "[" + PREFIX_TAG + "<tag>]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Leong Hong Fai "
            + PREFIX_ID + "E0543948 "
            + PREFIX_GROUP + "T01A "
            + PREFIX_GROUP + "R02B "
            + PREFIX_TAG + "beginner";

    public static final String MESSAGE_SUCCESS = "New student added: %1$s";
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student ID already exists in Source Control. ";

    private final Student toAdd;

    /**
     * Creates an AddStudentCommand to add the specified {@code Student}
     */
    public AddStudentCommand(Student student) {
        requireNonNull(student);
        toAdd = student;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasStudent(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT);
        }

        model.addStudent(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddStudentCommand // instanceof handles nulls
                && toAdd.equals(((AddStudentCommand) other).toAdd));
    }
}
