package seedu.sourcecontrol.logic;

import java.nio.file.Path;

import javafx.collections.ObservableList;
import seedu.sourcecontrol.commons.core.GuiSettings;
import seedu.sourcecontrol.logic.commands.CommandResult;
import seedu.sourcecontrol.logic.commands.exceptions.CommandException;
import seedu.sourcecontrol.logic.parser.exceptions.ParseException;
import seedu.sourcecontrol.model.ReadOnlySourceControl;
import seedu.sourcecontrol.model.student.Student;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the SourceControl.
     *
     * @see seedu.sourcecontrol.model.Model#getSourceControl()
     */
    ReadOnlySourceControl getSourceControl();

    /** Returns an unmodifiable view of the filtered list of students */
    ObservableList<Student> getFilteredStudentList();

    /**
     * Returns the user prefs' source control file path.
     */
    Path getSourceControlFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
