package seedu.sourcecontrol.model;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.sourcecontrol.commons.core.GuiSettings;
import seedu.sourcecontrol.logic.parser.Alias;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.group.Group;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Student> PREDICATE_SHOW_ALL_STUDENTS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' Source Control file path.
     */
    Path getSourceControlFilePath();

    /**
     * Sets the user prefs' Source Control file path.
     */
    void setSourceControlFilePath(Path sourceControlFilePath);

    /**
     * Sets the user prefs' aliases.
     */
    void setAliases(Map<String, String> aliases);

    /**
     * Returns the user prefs' aliases.
     */
    Map<String, String> getAliases();

    /**
     * Adds an alias to the user prefs' aliases.
     */
    void addAlias(Alias alias);

    /**
     * Removes an alias from the user pref's aliases.
     */
    void removeAlias(String aliasWord);

    /**
     * Replaces Source Control data with the data in {@code sourceControl}.
     */
    void setSourceControl(ReadOnlySourceControl sourceControl);

    /** Returns the SourceControl */
    ReadOnlySourceControl getSourceControl();

    /**
     * Returns true if a student with the same identity as {@code student} exists in the Source Control application.
     */
    boolean hasStudent(Student student);

    /**
     * Deletes the given student.
     * The student must exist in the Source Control application.
     */
    void deleteStudent(Student target);

    /**
     * Adds the given student.
     * {@code student} must not already exist in the Source Control application.
     */
    void addStudent(Student student);

    /**
     * Replaces the given student {@code target} with {@code editedStudent}.
     * {@code target} must exist in the Source Control application.
     * The student identity of {@code editedStudent} must not be the same as
     * another existing student in the Source Control application.
     */
    void setStudent(Student target, Student editedStudent);

    /**
     * Returns true if a group with the same identity as {@code group} exists in the application.
     */
    boolean hasGroup(Group group);

    /**
     * Adds the given group.
     * {@code group} must not already exist in the Source Control application.
     */
    void addGroup(Group group);

    /**
     * Returns true if a group with the same identity as {@code assessment} exists in the application.
     */
    boolean hasAssessment(Assessment assessment);

    /**
     * Returns assessment in Source Control with same identity as {@code assessmentToMatch} if exists.
     */
    Assessment getAssessment(Assessment assessmentToMatch);

    /**
     * Adds the given assessment.
     * {@code assessment} must not already exist in the Source Control application.
     */
    void addAssessment(Assessment assessment);

    /**
     * Replaces the given assessment {@code target} with {@code editedAssessment}.
     * {@code target} must exist in the Source Control application.
     * The assessment identity of {@code editedAssessment} must not be the same as
     * another existing assessment in the Source Control application.
     */
    void setAssessment(Assessment target, Assessment editedAssessment);

    /**
     * Returns group in Source Control with same identity as {@code groupToMatch} if exists.
     */
    Group getGroup(Group groupToMatch);

    /** Returns an unmodifiable view of the filtered student list */
    ObservableList<Student> getFilteredStudentList();

    /**
     * Updates the filter of the filtered student list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredStudentList(Predicate<Student> predicate);
}
