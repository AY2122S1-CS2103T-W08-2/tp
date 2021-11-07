package seedu.sourcecontrol.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ASSESSMENT;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_FILE;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.sourcecontrol.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.sourcecontrol.commons.core.Messages;
import seedu.sourcecontrol.commons.core.index.Index;
import seedu.sourcecontrol.logic.commands.exceptions.CommandException;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.AssessmentStatistics;
import seedu.sourcecontrol.model.student.assessment.GroupStatistics;
import seedu.sourcecontrol.model.student.assessment.StudentStatistics;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.id.IdContainsKeywordsPredicate;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.model.student.name.NameEqualsPredicate;

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
            + PREFIX_GROUP + "<group_name>) "
            + "[ " + PREFIX_FILE + "]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Tang Zhiying, "
            + COMMAND_WORD + " "
            + PREFIX_ASSESSMENT + "Midterm";

    public static final String MESSAGE_SUCCESS = "Info requested successfully. ";
    public static final String MESSAGE_NONEXISTENT_STUDENT = "This student does not exist.";
    public static final String MESSAGE_NONEXISTENT_ASSESSMENT = "This assessment does not exist.";
    public static final String MESSAGE_NONEXISTENT_GROUP = "This group does not exist.";
    public static final String MESSAGE_DUPLICATE_STUDENT_NAME =
            "This student needs to be specified using INDEX or ID due to duplicate naming.";

    public static final String BASE_PATH = "graph%1$s.png";

    private Index index;
    private Name name;
    private Id id;
    private Assessment assessment;
    private Group group;
    private Path savePath;

    /**
     * Constructor for a {@code ShowCommand} with given {@code Index}.
     */
    public ShowCommand(Index index, Path savePath) {
        requireNonNull(index);
        setIndex(index);
        setSavePath(savePath);
    }

    /**
     * Constructor for a {@code ShowCommand} with given {@code Name}.
     */
    public ShowCommand(Name name, Path savePath) {
        requireNonNull(name);
        setName(name);
        setSavePath(savePath);
    }

    /**
     * Constructor for a {@code ShowCommand} with given {@code Id}.
     */
    public ShowCommand(Id id, Path savePath) {
        requireNonNull(id);
        setId(id);
        setSavePath(savePath);
    }

    /**
     * Constructor for a {@code ShowCommand} with given {@code Assessment}.
     */
    public ShowCommand(Assessment assessment, Path savePath) {
        requireNonNull(assessment);
        setAssessment(assessment);
        setSavePath(savePath);
    }

    /**
     * Constructor for a {@code ShowCommand} with given {@code Group}.
     */
    public ShowCommand(Group group, Path savePath) {
        requireNonNull(group);
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
     * Executes command when a {@code Student} info is requested by an {@code Index}.
     */
    private CommandResult showStudentByIndex(Model model) throws CommandException {
        assert getIndex().isPresent();

        List<Student> students = model.getFilteredStudentList();

        if (index.getZeroBased() >= students.size()) {
            throw new CommandException(
                    String.format(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX, students.size()));
        }

        Student matchedStudent = students.get(index.getZeroBased());

        Info info = new Info(matchedStudent);
        StudentStatistics statistics = new StudentStatistics(matchedStudent);
        return new CommandResult(MESSAGE_SUCCESS, info, statistics.toLineChart(), savePath);
    }

    /**
     * Executes command when a {@code Student} info is requested by a {@code Name} or an {@code Id}.
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
        return new CommandResult(MESSAGE_SUCCESS, info, statistics.toLineChart(), savePath);
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
        GroupStatistics statistics = new GroupStatistics(matchedGroup, model.getSourceControl().getAssessmentList());
        return new CommandResult(MESSAGE_SUCCESS, info, statistics.toLineChart(), savePath);
    }

    /**
     * Creates a {@code Predicate} checking if a student has a matched
     * {@code Name}, or {@code Id}, or belonging to a {@code Group}.
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
                    .map(Id::toString).collect(Collectors.toList());
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
        boolean isSavePathEquals = Objects.equals(savePath, toCompare.savePath);

        return isIndexEquals && isNameEquals && isIdEquals
                && isAssessmentEquals && isGroupEquals && isSavePathEquals;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setId(Id id) {
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

    public Optional<Id> getId() {
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
