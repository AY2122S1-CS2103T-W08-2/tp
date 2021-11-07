package seedu.sourcecontrol.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.sourcecontrol.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.sourcecontrol.commons.core.Messages;
import seedu.sourcecontrol.commons.core.index.Index;
import seedu.sourcecontrol.commons.util.CollectionUtil;
import seedu.sourcecontrol.logic.commands.exceptions.CommandException;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.Score;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.model.student.tag.Tag;

/**
 * Edits the details of an existing student in the Source Control application.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the student identified "
            + "by the index number used in the displayed student list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: <index> (must be a positive integer) "
            + "[" + PREFIX_NAME + "<student_name>] "
            + "[" + PREFIX_ID + "<student_id>] "
            + "[" + PREFIX_GROUP + "<group_name>]... "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ID + "E0656831";

    public static final String MESSAGE_EDIT_STUDENT_SUCCESS = "Edited Student: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student already exists in Source Control.";
    public static final String MESSAGE_NONEXISTENT_GROUP = "This group does not exist.";

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
        List<Group> groupList = model.getSourceControl().getGroupList();

        if (!groupList.containsAll(editStudentDescriptor.getGroups().orElse(new ArrayList<>()))) {
            throw new CommandException(MESSAGE_NONEXISTENT_GROUP);
        }

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(
                    String.format(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX, lastShownList.size()));
        }

        Student studentToEdit = lastShownList.get(index.getZeroBased());
        Student editedStudent = createEditedStudent(studentToEdit, editStudentDescriptor, groupList);

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
    private static Student createEditedStudent(Student studentToEdit, EditStudentDescriptor editStudentDescriptor,
                                               List<Group> groups) {
        assert studentToEdit != null;

        Name updatedName = editStudentDescriptor.getName().orElse(studentToEdit.getName());
        Id updatedId = editStudentDescriptor.getId().orElse(studentToEdit.getId());

        // ensure that the groups added are the same as those in the group list
        List<Group> updatedGroups = editStudentDescriptor.getGroups().orElse(studentToEdit.getGroups());
        updatedGroups = updatedGroups.stream()
                .map(group -> groups.get(groups.indexOf(group)))
                .collect(Collectors.toList());

        // it is not the job of the edit command to edit scores
        Map<Assessment, Score> updatedScores = studentToEdit.getScores();
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
        private Id id;
        private List<Group> groups;
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
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, id, groups, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setId(Id id) {
            this.id = id;
        }

        public Optional<Id> getId() {
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
                    && getTags().equals(e.getTags());
        }
    }
}
