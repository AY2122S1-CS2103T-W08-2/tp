package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.equalsIgnoreOrder;
import static seedu.address.logic.commands.AddAllocCommand.AllocDescriptor;
import static seedu.address.logic.commands.AddAllocCommand.createEditedStudent;
import static seedu.address.logic.commands.AddAllocCommand.getAllocStudents;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Group;
import seedu.address.model.student.Name;
import seedu.address.model.student.NameEqualsPredicate;
import seedu.address.model.student.Student;

/**
 * Adds a group to the Source Control application.
 */
public class AddGroupCommand extends Command {

    public static final String COMMAND_WORD = "addgroup";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Creates a new group with the specified students (if any). "
            + "Parameters: "
            + PREFIX_GROUP + "<group_name> "
            + "[(" + PREFIX_NAME + "<student_name>" + " | "
            + PREFIX_ID + "<student_id>" + ")]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_GROUP + "T01A"
            + PREFIX_NAME + "John Doe "
            + PREFIX_ID + "E0543948";

    public static final String MESSAGE_SUCCESS = "New group added: %1$s\n";
    public static final String MESSAGE_STUDENTS_ADDED = "Students added to group: %1$s\n";
    public static final String MESSAGE_NONEXISTENT_STUDENT = "Student with name or ID \"%1$s\" does not exist.";
    public static final String MESSAGE_DUPLICATE_GROUP = "This group already exists in the application.";
    public static final String MESSAGE_DUPLICATE_STUDENT =
            "The student \"%1$s\" needs to be allocated manually using ID due to duplicate naming.";
    public static final String MESSAGE_DUPLICATE_STUDENT_IN_GROUP =
            "The student \"%1$s\" (%2$s) was specified more than once.";

    private final Group toAdd;
    private final List<AllocDescriptor> allocDescriptors;

    /**
     * Creates an AddGroupCommand to add the specified {@code Group}
     */
    public AddGroupCommand(Group group, List<AllocDescriptor> allocDescriptors) {
        requireNonNull(group);
        requireNonNull(allocDescriptors);
        toAdd = group;
        this.allocDescriptors = new ArrayList<>(allocDescriptors);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasGroup(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_GROUP);
        }

        List<Student> addedStudents = new ArrayList<>();

        for (AllocDescriptor allocDescriptor : allocDescriptors) {
            List<Student> matchedStudents = getAllocStudents(model.getAddressBook().getStudentList(), allocDescriptor);

            if (matchedStudents.isEmpty()) {
                String studentKey = allocDescriptor.getName().isPresent()
                        ? allocDescriptor.getName().get().toString()
                        : allocDescriptor.getId().get().toString();
                throw new CommandException(String.format(MESSAGE_NONEXISTENT_STUDENT, studentKey));
            }

            Student studentToEdit = matchedStudents.get(0);

            if (matchedStudents.size() > 1) {
                List<String> matchedIds = matchedStudents.stream()
                        .map(Student::getName).map(Name::toString)
                        .collect(Collectors.toList());
                Predicate<Student> predicate = new NameEqualsPredicate(matchedIds.get(0));
                model.updateFilteredStudentList(predicate);
                throw new CommandException(String.format(MESSAGE_DUPLICATE_STUDENT, studentToEdit.getName()));
            }

            if (toAdd.hasStudent(studentToEdit.getId())) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_STUDENT_IN_GROUP,
                        studentToEdit.getName(), studentToEdit.getId()));
            }

            Student editedStudent = createEditedStudent(studentToEdit, allocDescriptor);
            toAdd.addStudent(editedStudent.getId());
            addedStudents.add(editedStudent);
            model.setStudent(studentToEdit, editedStudent);
        }

        model.addGroup(toAdd);
        return new CommandResult(formatSuccessMessage(addedStudents));
    }

    /**
     * Returns the formatted success message, depending on whether there were students added to the new group.
     */
    public String formatSuccessMessage(List<Student> addedStudents) {
        String groupAddedMessage = String.format(MESSAGE_SUCCESS, toAdd.name);

        if (addedStudents.isEmpty()) {
            return groupAddedMessage;
        }

        String studentNames = addedStudents.stream()
                .map(student -> student.getName().fullName)
                .collect(Collectors.joining(", "));

        return groupAddedMessage + String.format(MESSAGE_STUDENTS_ADDED, studentNames);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddGroupCommand)) {
            return false;
        }

        // state check
        AddGroupCommand e = (AddGroupCommand) other;

        return toAdd.equals(e.toAdd)
                && equalsIgnoreOrder(allocDescriptors, e.allocDescriptors);
    }
}
