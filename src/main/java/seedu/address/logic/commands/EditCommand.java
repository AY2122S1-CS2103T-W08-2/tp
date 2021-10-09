package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Assessment;
import seedu.address.model.student.Group;
import seedu.address.model.student.ID;
import seedu.address.model.student.Name;
import seedu.address.model.student.Score;
import seedu.address.model.student.Student;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing student in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the student identified "
            + "by the index number used in the displayed student list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "<student_name>] "
            + "[" + PREFIX_ID + "<student_id>] "
            + "[" + PREFIX_GROUP + "<group_name>]... "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ID + "E0656831";

    public static final String MESSAGE_EDIT_STUDENT_SUCCESS = "Edited Student: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student already exists in the address book.";

    private final Index index;
    private final EditStudentDescriptor editStudentDescriptor;

    /**
     * @param index of the student in the filtered student list to edit
     * @param editStudentDescriptor details to edit the student with
     */
    public EditCommand(Index index, EditStudentDescriptor editStudentDescriptor) {
        requireNonNull(index);
        requireNonNull(editStudentDescriptor);

        this.index = index;
        this.editStudentDescriptor = new EditStudentDescriptor(editStudentDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student studentToEdit = lastShownList.get(index.getZeroBased());
        Student editedStudent = createEditedStudent(studentToEdit, editStudentDescriptor);

        if (!studentToEdit.isSameStudent(editedStudent) && model.hasStudent(editedStudent)) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT);
        }

        model.setStudent(studentToEdit, editedStudent);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        return new CommandResult(String.format(MESSAGE_EDIT_STUDENT_SUCCESS, editedStudent));
    }

    /**
     * Creates and returns a {@code Student} with the details of {@code studentToEdit}
     * edited with {@code editStudentDescriptor}.
     */
    private static Student createEditedStudent(Student studentToEdit, EditStudentDescriptor editStudentDescriptor) {
        assert studentToEdit != null;

        Name updatedName = editStudentDescriptor.getName().orElse(studentToEdit.getName());
        ID updatedId = editStudentDescriptor.getId().orElse(studentToEdit.getId());

        List<Group> updatedGroups = editStudentDescriptor.getGroups().orElse(studentToEdit.getGroups());
        Map<Assessment, Score> updatedScores = editStudentDescriptor.getScores().orElse(studentToEdit.getScores());
        Set<Tag> updatedTags = editStudentDescriptor.getTags().orElse(studentToEdit.getTags());

        return new Student(updatedName, updatedId, updatedGroups, updatedScores, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editStudentDescriptor.equals(e.editStudentDescriptor);
    }

    /**
     * Stores the details to edit the student with. Each non-empty field value will replace the
     * corresponding field value of the student.
     */
    public static class EditStudentDescriptor {
        private Name name;
        private ID id;
        private List<Group> groups;
        private Map<Assessment, Score> scores;
        private Set<Tag> tags;

        public EditStudentDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditStudentDescriptor(EditStudentDescriptor toCopy) {
            setName(toCopy.name);
            setId(toCopy.id);
            setGroups(toCopy.groups);
            setScores(toCopy.scores);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, id, groups, scores, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setId(ID id) {
            this.id = id;
        }

        public Optional<ID> getId() {
            return Optional.ofNullable(id);
        }

        /**
         * Sets {@code groups} to this object's {@code groups}.
         * A defensive copy of {@code groups} is used internally.
         */
        public void setGroups(List<Group> groups) {
            this.groups = (groups != null) ? groups.stream().distinct().collect(Collectors.toList()) : null;
        }

        /**
         * Returns an unmodifiable group list, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code groups} is null.
         */
        public Optional<List<Group>> getGroups() {
            return (groups != null) ? Optional.of(Collections.unmodifiableList(groups)) : Optional.empty();
        }

        /**
         * Sets {@code scores} to this object's {@code scores}.
         * A defensive copy of {@code scores} is used internally.
         */
        public void setScores(Map<Assessment, Score> scores) {
            this.scores = (scores != null) ? new HashMap<>(scores) : null;
        }

        /**
         * Returns an unmodifiable score map, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code scores} is null.
         */
        public Optional<Map<Assessment, Score>> getScores() {
            return (scores != null) ? Optional.of(Collections.unmodifiableMap(scores)) : Optional.empty();
        }


        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditStudentDescriptor)) {
                return false;
            }

            // state check
            EditStudentDescriptor e = (EditStudentDescriptor) other;

            return getName().equals(e.getName())
                    && getId().equals(e.getId())
                    && getGroups().equals(e.getGroups())
                    && getScores().equals(e.getScores())
                    && getTags().equals(e.getTags());
        }
    }
}
