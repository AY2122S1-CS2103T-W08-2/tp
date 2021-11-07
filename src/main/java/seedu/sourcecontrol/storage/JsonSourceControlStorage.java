package seedu.sourcecontrol.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.sourcecontrol.commons.core.LogsCenter;
import seedu.sourcecontrol.commons.exceptions.DataConversionException;
import seedu.sourcecontrol.commons.exceptions.IllegalValueException;
import seedu.sourcecontrol.commons.util.FileUtil;
import seedu.sourcecontrol.commons.util.JsonUtil;
import seedu.sourcecontrol.model.ReadOnlySourceControl;

/**
 * A class to access SourceControl data stored as a json file on the hard disk.
 */
public class JsonSourceControlStorage implements SourceControlStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonSourceControlStorage.class);

    private Path filePath;

    public JsonSourceControlStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getSourceControlFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlySourceControl> readSourceControl() throws DataConversionException {
        return readSourceControl(filePath);
    }

    /**
     * Similar to {@link #readSourceControl()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlySourceControl> readSourceControl(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableSourceControl> jsonSourceControl = JsonUtil.readJsonFile(
                filePath, JsonSerializableSourceControl.class);
        if (!jsonSourceControl.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonSourceControl.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveSourceControl(ReadOnlySourceControl sourceControl) throws IOException {
        saveSourceControl(sourceControl, filePath);
    }

    /**
     * Similar to {@link #saveSourceControl(ReadOnlySourceControl)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveSourceControl(ReadOnlySourceControl sourceControl, Path filePath) throws IOException {
        requireNonNull(sourceControl);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableSourceControl(sourceControl), filePath);
    }

}
