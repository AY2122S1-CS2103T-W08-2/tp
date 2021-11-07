package seedu.sourcecontrol.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.sourcecontrol.commons.exceptions.DataConversionException;
import seedu.sourcecontrol.model.ReadOnlySourceControl;
import seedu.sourcecontrol.model.ReadOnlyUserPrefs;
import seedu.sourcecontrol.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends SourceControlStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getSourceControlFilePath();

    @Override
    Optional<ReadOnlySourceControl> readSourceControl() throws DataConversionException, IOException;

    @Override
    void saveSourceControl(ReadOnlySourceControl sourceControl) throws IOException;

}
