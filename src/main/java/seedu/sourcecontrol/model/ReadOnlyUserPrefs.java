package seedu.sourcecontrol.model;

import java.nio.file.Path;
import java.util.Map;

import seedu.sourcecontrol.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getSourceControlFilePath();

    Map<String, String> getAliases();

}
