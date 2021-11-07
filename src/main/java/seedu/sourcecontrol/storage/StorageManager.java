package seedu.sourcecontrol.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.sourcecontrol.commons.core.LogsCenter;
import seedu.sourcecontrol.commons.exceptions.DataConversionException;
import seedu.sourcecontrol.model.ReadOnlySourceControl;
import seedu.sourcecontrol.model.ReadOnlyUserPrefs;
import seedu.sourcecontrol.model.UserPrefs;

/**
 * Manages storage of SourceControl data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private SourceControlStorage sourceControlStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code SourceControlStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(SourceControlStorage sourceControlStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.sourceControlStorage = sourceControlStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ SourceControl methods ==============================

    @Override
    public Path getSourceControlFilePath() {
        return sourceControlStorage.getSourceControlFilePath();
    }

    @Override
    public Optional<ReadOnlySourceControl> readSourceControl() throws DataConversionException, IOException {
        return readSourceControl(sourceControlStorage.getSourceControlFilePath());
    }

    @Override
    public Optional<ReadOnlySourceControl> readSourceControl(Path filePath)
            throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return sourceControlStorage.readSourceControl(filePath);
    }

    @Override
    public void saveSourceControl(ReadOnlySourceControl sourceControl) throws IOException {
        saveSourceControl(sourceControl, sourceControlStorage.getSourceControlFilePath());
    }

    @Override
    public void saveSourceControl(ReadOnlySourceControl sourceControl, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        sourceControlStorage.saveSourceControl(sourceControl, filePath);
    }

}
