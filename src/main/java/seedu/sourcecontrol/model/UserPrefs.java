package seedu.sourcecontrol.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import seedu.sourcecontrol.commons.core.GuiSettings;
import seedu.sourcecontrol.commons.util.FileUtil;
import seedu.sourcecontrol.logic.parser.Alias;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    private GuiSettings guiSettings = new GuiSettings();
    private Path sourceControlFilePath = FileUtil.pathOf("data" , "sourcecontrol.json");
    private Map<String, String> aliases = new HashMap<>();

    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {}

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setSourceControlFilePath(newUserPrefs.getSourceControlFilePath());
        setAliases(newUserPrefs.getAliases());
    }

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    public Path getSourceControlFilePath() {
        return sourceControlFilePath;
    }

    public void setSourceControlFilePath(Path sourceControlFilePath) {
        requireNonNull(sourceControlFilePath);
        this.sourceControlFilePath = sourceControlFilePath;
    }

    public Map<String, String> getAliases() {
        return aliases;
    }

    public void setAliases(Map<String, String> aliases) {
        requireNonNull(aliases);
        this.aliases = new HashMap<>(aliases);
    }

    public void addAlias(Alias alias) {
        this.aliases.put(alias.getAliasWord(), alias.getCommandWord());
    }

    public void removeAlias(String aliasWord) {
        this.aliases.remove(aliasWord);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return guiSettings.equals(o.guiSettings)
                && sourceControlFilePath.equals(o.sourceControlFilePath)
                && aliases.equals(o.aliases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, sourceControlFilePath, aliases);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings);
        sb.append("\nLocal data file location : " + sourceControlFilePath);
        return sb.toString();
    }

}
