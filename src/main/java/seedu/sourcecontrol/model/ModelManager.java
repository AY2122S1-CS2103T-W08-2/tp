package seedu.sourcecontrol.model;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.sourcecontrol.commons.core.GuiSettings;
import seedu.sourcecontrol.commons.core.LogsCenter;
import seedu.sourcecontrol.logic.parser.Alias;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.group.Group;

/**
 * Represents the in-memory model of the Source Control data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final SourceControl sourceControl;
    private final UserPrefs userPrefs;
    private final FilteredList<Student> filteredStudents;

    /**
     * Initializes a ModelManager with the given Source Control and userPrefs.
     */
    public ModelManager(ReadOnlySourceControl sourceControl, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(sourceControl, userPrefs);

        logger.fine("Initializing with Source Control: " + sourceControl + " and user prefs " + userPrefs);

        this.sourceControl = new SourceControl(sourceControl);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredStudents = new FilteredList<>(this.sourceControl.getStudentList());
    }

    public ModelManager() {
        this(new SourceControl(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getSourceControlFilePath() {
        return userPrefs.getSourceControlFilePath();
    }

    @Override
    public void setSourceControlFilePath(Path sourceControlFilePath) {
        requireNonNull(sourceControlFilePath);
        userPrefs.setSourceControlFilePath(sourceControlFilePath);
    }

    @Override
    public Map<String, String> getAliases() {
        return userPrefs.getAliases();
    }

    @Override
    public void setAliases(Map<String, String> aliases) {
        userPrefs.setAliases(aliases);
    }

    @Override
    public void addAlias(Alias alias) {
        userPrefs.addAlias(alias);
    }

    @Override
    public void removeAlias(String aliasWord) {
        userPrefs.removeAlias(aliasWord);
    }

    //=========== SourceControl ================================================================================

    @Override
    public void setSourceControl(ReadOnlySourceControl sourceControl) {
        this.sourceControl.resetData(sourceControl);
    }

    @Override
    public ReadOnlySourceControl getSourceControl() {
        return sourceControl;
    }

    @Override
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return sourceControl.hasStudent(student);
    }

    @Override
    public void deleteStudent(Student target) {
        sourceControl.removeStudent(target);
    }

    @Override
    public void addStudent(Student student) {
        sourceControl.addStudent(student);
        updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
    }

    @Override
    public void setStudent(Student target, Student editedStudent) {
        requireAllNonNull(target, editedStudent);

        sourceControl.setStudent(target, editedStudent);
    }

    @Override
    public boolean hasGroup(Group group) {
        requireNonNull(group);
        return sourceControl.hasGroup(group);
    }

    @Override
    public void addGroup(Group group) {
        sourceControl.addGroup(group);
        updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
    }

    @Override
    public Group getGroup(Group groupToMatch) {
        requireNonNull(groupToMatch);
        return sourceControl.getGroup(groupToMatch);
    }

    @Override
    public boolean hasAssessment(Assessment assessment) {
        requireNonNull(assessment);
        return sourceControl.hasAssessment(assessment);
    }

    @Override
    public Assessment getAssessment(Assessment assessmentToMatch) {
        requireNonNull(assessmentToMatch);
        return sourceControl.getAssessment(assessmentToMatch);
    }

    @Override
    public void addAssessment(Assessment assessment) {
        sourceControl.addAssessment(assessment);
        updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
    }

    @Override
    public void setAssessment(Assessment target, Assessment editedAssessment) {
        requireAllNonNull(target, editedAssessment);

        sourceControl.setAssessment(target, editedAssessment);
    }

    //=========== Filtered Student List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Student} backed by the internal list of
     * {@code versionedSourceControl}
     */
    @Override
    public ObservableList<Student> getFilteredStudentList() {
        return filteredStudents;
    }

    @Override
    public void updateFilteredStudentList(Predicate<Student> predicate) {
        requireNonNull(predicate);
        filteredStudents.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return sourceControl.equals(other.sourceControl)
                && userPrefs.equals(other.userPrefs)
                && filteredStudents.equals(other.filteredStudents);
    }

}
