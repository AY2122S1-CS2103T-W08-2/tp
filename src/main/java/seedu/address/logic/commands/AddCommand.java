package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;

/**
 * Adds a student to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add student";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a student to the database. "
            + "Parameters: "
            + PREFIX_NAME + "<student_name> "
            + PREFIX_ID + "<student_id> "
            + "[" + PREFIX_GROUP + "<group_name>]... "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_ID + "E0543948 "
            + PREFIX_GROUP + "T01A"
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New student added: %1$s";
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student already exists in the address book";

    private final Student toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Student}
     */
    public AddCommand(Student student) {
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
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
