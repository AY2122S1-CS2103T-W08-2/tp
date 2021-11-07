package seedu.sourcecontrol.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.sourcecontrol.commons.exceptions.DataConversionException;
import seedu.sourcecontrol.model.ReadOnlySourceControl;
import seedu.sourcecontrol.model.SourceControl;

/**
 * Represents a storage for {@link SourceControl}.
 */
public interface SourceControlStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getSourceControlFilePath();

    /**
     * Returns SourceControl data as a {@link ReadOnlySourceControl}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlySourceControl> readSourceControl() throws DataConversionException, IOException;

    /**
     * @see #getSourceControlFilePath()
     */
    Optional<ReadOnlySourceControl> readSourceControl(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlySourceControl} to the storage.
     * @param sourceControl cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveSourceControl(ReadOnlySourceControl sourceControl) throws IOException;

    /**
     * @see #saveSourceControl(ReadOnlySourceControl)
     */
    void saveSourceControl(ReadOnlySourceControl sourceControl, Path filePath) throws IOException;

}
