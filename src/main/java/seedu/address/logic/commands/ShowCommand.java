package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSESSMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.*;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Assessment;
import seedu.address.model.student.AssessmentStatistics;
import seedu.address.model.student.Group;
import seedu.address.model.student.GroupStatistics;
import seedu.address.model.student.ID;
import seedu.address.model.student.IdContainsKeywordsPredicate;
import seedu.address.model.student.Name;
import seedu.address.model.student.NameEqualsPredicate;
import seedu.address.model.student.Student;
import seedu.address.model.student.StudentStatistics;
import seedu.address.commons.util.AdvCandleStickChart;

/**
 * Shows information of a student or an assessment.
 */
public class ShowCommand extends Command {

    public static final String COMMAND_WORD = "show";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows performance analysis of a student, an assessment or a group. "
            + "Parameters: "
            + "(<index> | "
            + PREFIX_NAME + "<student_name> | "
            + PREFIX_ID + "<student_id> | "
            + PREFIX_ASSESSMENT + "<assessment_name> | "
            + PREFIX_GROUP + "<group_name>)"
            + "[" + PREFIX_FILE + "<export_location>]";

    public static final String MESSAGE_SUCCESS = "Info requested successfully";
    public static final String MESSAGE_NONEXISTENT_STUDENT = "This student does not exist.";
    public static final String MESSAGE_NONEXISTENT_ASSESSMENT = "This assessment does not exist.";
    public static final String MESSAGE_NONEXISTENT_GROUP = "This group does not exist.";
    public static final String MESSAGE_DUPLICATE_STUDENT_NAME =
            "This student needs to be specified using INDEX or ID due to duplicate naming.";

    private Index index;
    private Name name;
    private ID id;
    private Assessment assessment;
    private Group group;
    private Path savePath;

    /**
     * Constructor for a {@code ShowCommand} with given {@code Index}.
     */
    public ShowCommand(Index index, Path savePath) {
        setIndex(index);
        setSavePath(savePath);
    }

    /**
     * Constructor for a {@code ShowCommand} with given {@code Name}.
     */
    public ShowCommand(Name name, Path savePath) {
        setName(name);
        setSavePath(savePath);
    }

    /**
     * Constructor for a {@code ShowCommand} with given {@code ID}.
     */
    public ShowCommand(ID id, Path savePath) {
        setId(id);
        setSavePath(savePath);
    }

    /**
     * Constructor for a {@code ShowCommand} with given {@code Assessment}.
     */
    public ShowCommand(Assessment assessment, Path savePath) {
        setAssessment(assessment);
        setSavePath(savePath);
    }

    /**
     * Constructor for a {@code ShowCommand} with given {@code ShowCommand}.
     */
    public ShowCommand(Group group, Path savePath) {
        setGroup(group);
        setSavePath(savePath);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        return getIndex().isPresent()
                ? showStudentByIndex(model)
                : getAssessment().isPresent()
                ? showAssessment(model)
                : getGroup().isPresent()
                ? showGroup(model)
                : showStudentByPrefixes(model);
    }

    /**
     * Executes command when a {@code Student} info is requested by index.
     */
    private CommandResult showStudentByIndex(Model model) throws CommandException {
        assert getIndex().isPresent();

        List<Student> students = model.getFilteredStudentList();

        if (index.getZeroBased() >= students.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student matchedStudent = students.get(index.getZeroBased());

        Info info = new Info(matchedStudent);
        StudentStatistics statistics = new StudentStatistics(matchedStudent);
        return new CommandResult(MESSAGE_SUCCESS, info, statistics.toLineChart(), savePath);
    }

    /**
     * Executes command when a {@code Student} info is requested by name or ID.
     */
    private CommandResult showStudentByPrefixes(Model model) throws CommandException {
        assert getName().isPresent() || getId().isPresent();

        // filter student list into students with matched identity
        Predicate<Student> predicate = createStudentPredicate();
        model.updateFilteredStudentList(predicate);
        List<Student> matchedStudents = model.getFilteredStudentList();

        if (matchedStudents.size() == 0) {
            throw new CommandException(MESSAGE_NONEXISTENT_STUDENT);
        }

        if (matchedStudents.size() > 1) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT_NAME);
        }

        Student matchedStudent = matchedStudents.get(0);

        Info info = new Info(matchedStudent);
        StudentStatistics statistics = new StudentStatistics(matchedStudent);
        return new CommandResult(MESSAGE_SUCCESS, info, new AdvCandleStickChart(), savePath);
    }

    /**
     * Executes command when an {@code Assessment} info is requested.
     */
    private CommandResult showAssessment(Model model) throws CommandException {
        assert getAssessment().isPresent();
        Assessment matchedAssessment = model.getAssessment(assessment);

        // reset filtered student list (if any) into normal list
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);

        if (matchedAssessment == null) {
            throw new CommandException(MESSAGE_NONEXISTENT_ASSESSMENT);
        }

        Info info = new Info(matchedAssessment);
        AssessmentStatistics statistics = new AssessmentStatistics(matchedAssessment);
        return new CommandResult(MESSAGE_SUCCESS, info, statistics.toHistogram(), savePath);
    }

    /**
     * Executes command when a {@code Group} info is requested.
     */
    private CommandResult showGroup(Model model) throws CommandException {
        assert getGroup().isPresent();
        Group matchedGroup = model.getGroup(group);

        if (matchedGroup == null) {
            throw new CommandException(MESSAGE_NONEXISTENT_GROUP);
        }

        // filter student list into students in matched group
        // (to allow easier reference to students in the interest group)
        group = matchedGroup;
        Predicate<Student> predicate = createStudentPredicate();
        model.updateFilteredStudentList(predicate);

        Info info = new Info(matchedGroup);
        GroupStatistics statistics = new GroupStatistics(matchedGroup, model);
        return new CommandResult(MESSAGE_SUCCESS, info, statistics.toLineChart(), savePath);
    }

    /**
     * Creates a {@code Predicate} checking if a student has a matched name or ID.
     */
    private Predicate<Student> createStudentPredicate() {
        if (getName().isPresent()) {
            return new NameEqualsPredicate(name.fullName);
        }

        if (getId().isPresent()) {
            return new IdContainsKeywordsPredicate(List.of(id.toString()));
        }

        if (getGroup().isPresent()) {
            List<String> ids = group.getStudents().stream()
                    .map(ID::toString).collect(Collectors.toList());
            return new IdContainsKeywordsPredicate(ids);
        }

        return null;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ShowCommand)) {
            return false;
        }

        // state check
        ShowCommand toCompare = (ShowCommand) other;
        boolean isIndexEquals = Objects.equals(index, toCompare.index);
        boolean isNameEquals = Objects.equals(name, toCompare.name);
        boolean isIdEquals = Objects.equals(id, toCompare.id);
        boolean isAssessmentEquals = Objects.equals(assessment, toCompare.assessment);
        boolean isGroupEquals = Objects.equals(group, toCompare.group);

        return isIndexEquals && isNameEquals && isIdEquals && isAssessmentEquals && isGroupEquals;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setSavePath(Path savePath) {
        this.savePath = savePath;
    }

    public Optional<Index> getIndex() {
        return Optional.ofNullable(index);
    }

    public Optional<Name> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<ID> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<Assessment> getAssessment() {
        return Optional.ofNullable(assessment);
    }

    public Optional<Group> getGroup() {
        return Optional.ofNullable(group);
    }

    public Optional<Path> getSavePath() {
        return Optional.ofNullable(savePath);
    }

    /**
     * Stores info of a student or an assessment.
     */
    public static class Info {
        private Student student;
        private Assessment assessment;
        private Group group;

        public Info(Student student) {
            setStudent(student);
        }

        public Info(Assessment assessment) {
            setAssessment(assessment);
        }

        public Info(Group group) {
            setGroup(group);
        }

        public void setStudent(Student student) {
            this.student = student;
        }

        public void setAssessment(Assessment assessment) {
            this.assessment = assessment;
        }

        public void setGroup(Group group) {
            this.group = group;
        }

        public Optional<Student> getStudent() {
            return Optional.ofNullable(student);
        }

        public Optional<Assessment> getAssessment() {
            return Optional.ofNullable(assessment);
        }

        public Optional<Group> getGroup() {
            return Optional.ofNullable(group);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof Info)) {
                return false;
            }

            // state check
            Info e = (Info) other;

            return getStudent().equals(e.getStudent())
                    && getAssessment().equals(e.getAssessment())
                    && getGroup().equals(e.getGroup());
        }
    }
}
