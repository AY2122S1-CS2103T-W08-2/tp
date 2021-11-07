package seedu.sourcecontrol.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ASSESSMENT;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_SCORE;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.sourcecontrol.logic.commands.exceptions.CommandException;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ReadOnlySourceControl;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.Score;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.model.student.name.NameEqualsPredicate;
import seedu.sourcecontrol.model.student.tag.Tag;

/**
 * Allocates a score for an existing assessment for an existing student.
 */
public class AddScoreCommand extends Command {

    public static final String COMMAND_WORD = "addscore";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds score of an assessment for a student. "
            + "Parameters: "
            + PREFIX_ASSESSMENT + "<assessment_name> "
            + "(" + PREFIX_NAME + "<student_name> | "
            + PREFIX_ID + "<student_id>) "
            + PREFIX_SCORE + "<score_in_percentage>\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ASSESSMENT + "Midterm "
            + PREFIX_NAME + "Tang Zhiying "
            + PREFIX_SCORE + "68.5";

    public static final String MESSAGE_ADD_SUCCESS = "New score added: %1$s";
    public static final String MESSAGE_UPDATE_SUCCESS = "Score updated from %1$s to %2$s: %3$s";
    public static final String MESSAGE_NONEXISTENT_ASSESSMENT = "This assessment does not exist.";
    public static final String MESSAGE_NONEXISTENT_STUDENT = "This student does not exist.";
    public static final String MESSAGE_DUPLICATE_STUDENT_NAME =
            "Score needs to be added through ID for this student due to duplicate naming.";

    private final ScoreDescriptor scoreDescriptor;

    /**
     * @param scoreDescriptor details of the score
     */
    public AddScoreCommand(ScoreDescriptor scoreDescriptor) {
        requireNonNull(scoreDescriptor);
        this.scoreDescriptor = new ScoreDescriptor(scoreDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        ReadOnlySourceControl sourceControl = model.getSourceControl();
        List<Assessment> assessmentList = sourceControl.getAssessmentList();
        List<Student> studentList = sourceControl.getStudentList();

        assert scoreDescriptor.getAssessment().isPresent();
        assert scoreDescriptor.getScore().isPresent();

        if (!assessmentList.contains(scoreDescriptor.getAssessment().get())) {
            throw new CommandException(MESSAGE_NONEXISTENT_ASSESSMENT);
        }

        Assessment assessmentToEdit = getToEditAssessment(assessmentList, scoreDescriptor);
        List<Student> studentsToEdit = getToEditStudents(studentList, scoreDescriptor);

        if (studentsToEdit.isEmpty()) {
            throw new CommandException(MESSAGE_NONEXISTENT_STUDENT);
        }

        if (studentsToEdit.size() > 1) {
            List<String> matchedIds = studentsToEdit.stream()
                    .map(Student::getName).map(Name::toString)
                    .collect(Collectors.toList());
            Predicate<Student> predicate = new NameEqualsPredicate(matchedIds.get(0));
            model.updateFilteredStudentList(predicate);
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT_NAME);
        }

        Student studentToEdit = studentsToEdit.get(0);

        boolean wasGraded = assessmentToEdit.isGraded(studentToEdit.getId());
        Score oldScore = null;
        if (wasGraded) {
            oldScore = assessmentToEdit.getScores().get(studentToEdit.getId());
        }

        Student editedStudent = createEditedStudents(studentToEdit, scoreDescriptor, assessmentToEdit);

        assessmentToEdit.setScore(editedStudent.getId(), scoreDescriptor.getScore().get());
        model.setStudent(studentToEdit, editedStudent);
        return new CommandResult(wasGraded
                ? String.format(MESSAGE_UPDATE_SUCCESS, oldScore, scoreDescriptor.getScore().get(), editedStudent)
                : String.format(MESSAGE_ADD_SUCCESS, editedStudent));
    }

    /**
     * Gets and returns a map of {@code Assessment} with matching info
     * as specified in the {@code scoreDescriptor}.
     */
    public static Assessment getToEditAssessment(List<Assessment> assessments, ScoreDescriptor scoreDescriptor) {
        List<Assessment> assessmentToEdit = assessments.stream()
                .filter(scoreDescriptor.isToEditAssessment())
                .collect(Collectors.toList());
        assert assessmentToEdit.size() == 1;
        return assessmentToEdit.get(0);
    }

    /**
     * Get and returns a list of {@code Student} with matching info
     * as specified in the {@code scoreDescriptor}.
     */
    public static List<Student> getToEditStudents(List<Student> students, ScoreDescriptor scoreDescriptor) {
        return students.stream()
                .filter(scoreDescriptor.isToEditStudent())
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Creates and returns a {@code Student} with the details of {@code preScoreStudent}
     * being added score as specified in {@code scoreDescriptor}.
     */
    public static Student createEditedStudents(Student toEditStudent, ScoreDescriptor scoreDescriptor,
                                               Assessment assessmentToEdit) {
        assert toEditStudent != null;
        assert scoreDescriptor.getScore().isPresent();
        assert assessmentToEdit != null;

        Name name = toEditStudent.getName();
        Id id = toEditStudent.getId();
        List<Group> groups = toEditStudent.getGroups();
        Map<Assessment, Score> scores = toEditStudent.getScores();
        Set<Tag> tags = toEditStudent.getTags();

        Score scoreToEdit = scoreDescriptor.getScore().get();

        Map<Assessment, Score> editedScores = new LinkedHashMap<>(scores);
        editedScores.put(assessmentToEdit, scoreToEdit);

        return new Student(name, id, groups, editedScores, tags);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddScoreCommand // instanceof handles nulls
                && scoreDescriptor.equals(((AddScoreCommand) other).scoreDescriptor));
    }

    /**
     * Stores the details to edit the student with. Each non-empty field value will replace the
     * corresponding field value of the student.
     */
    public static class ScoreDescriptor {
        private Name name;
        private Id id;
        private Assessment assessment;
        private Score score;

        public ScoreDescriptor() {}

        /**
         * Copy constructor.
         */
        public ScoreDescriptor(ScoreDescriptor toCopy) {
            setName(toCopy.name);
            setId(toCopy.id);
            setAssessment(toCopy.assessment);
            setScore(toCopy.score);
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

        public void setAssessment(Assessment assessment) {
            this.assessment = assessment;
        }

        public Optional<Assessment> getAssessment() {
            return Optional.ofNullable(assessment);
        }

        public void setScore(Score score) {
            this.score = score;
        }

        public Optional<Score> getScore() {
            return Optional.ofNullable(score);
        }

        /**
         * Returns a {@code Predicate} checking if an assessment have a matched name.
         */
        public Predicate<Assessment> isToEditAssessment() {
            return toCheck -> toCheck.equals(assessment);
        }

        /**
         * Returns a {@code Predicate} checking if a student have a matched name or ID.
         */
        public Predicate<Student> isToEditStudent() {
            return toCheck -> toCheck.getName().equals(name)
                    || toCheck.getId().equals(id);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof ScoreDescriptor)) {
                return false;
            }

            // state check
            ScoreDescriptor e = (ScoreDescriptor) other;

            return getName().equals(e.getName())
                    && getId().equals(e.getId())
                    && getAssessment().equals(e.getAssessment())
                    && getScore().equals(e.getScore());
        }
    }
}
