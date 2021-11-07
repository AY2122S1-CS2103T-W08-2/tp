package seedu.sourcecontrol.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;
import static seedu.sourcecontrol.testutil.TypicalStudents.ALICE;
import static seedu.sourcecontrol.testutil.TypicalStudents.HOON;
import static seedu.sourcecontrol.testutil.TypicalStudents.IDA;
import static seedu.sourcecontrol.testutil.TypicalStudents.getTypicalSourceControl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.sourcecontrol.commons.exceptions.DataConversionException;
import seedu.sourcecontrol.model.ReadOnlySourceControl;
import seedu.sourcecontrol.model.SourceControl;

public class JsonSourceControlStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSourceControlStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readSourceControl_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readSourceControl(null));
    }

    private java.util.Optional<ReadOnlySourceControl> readSourceControl(String filePath) throws Exception {
        return new JsonSourceControlStorage(Paths.get(filePath))
                .readSourceControl(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readSourceControl("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readSourceControl("notJsonFormatSourceControl.json"));
    }

    @Test
    public void readSourceControl_invalidStudentSourceControl_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readSourceControl("invalidStudentSourceControl.json"));
    }

    @Test
    public void readSourceControl_invalidAndValidStudentSourceControl_throwDataConversionException() {
        assertThrows(DataConversionException.class, () ->
                readSourceControl("invalidAndValidStudentSourceControl.json"));
    }

    @Test
    public void readAndSaveSourceControl_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempSourceControl.json");
        SourceControl original = getTypicalSourceControl();
        JsonSourceControlStorage jsonSourceControlStorage = new JsonSourceControlStorage(filePath);

        // Save in new file and read back
        jsonSourceControlStorage.saveSourceControl(original, filePath);
        ReadOnlySourceControl readBack = jsonSourceControlStorage.readSourceControl(filePath).get();
        assertEquals(original, new SourceControl(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addStudent(HOON);
        original.removeStudent(ALICE);
        jsonSourceControlStorage.saveSourceControl(original, filePath);
        readBack = jsonSourceControlStorage.readSourceControl(filePath).get();
        assertEquals(original, new SourceControl(readBack));

        // Save and read without specifying file path
        original.addStudent(IDA);
        jsonSourceControlStorage.saveSourceControl(original); // file path not specified
        readBack = jsonSourceControlStorage.readSourceControl().get(); // file path not specified
        assertEquals(original, new SourceControl(readBack));

    }

    @Test
    public void saveSourceControl_nullSourceControl_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveSourceControl(null, "SomeFile.json"));
    }

    /**
     * Saves {@code sourceControl} at the specified {@code filePath}.
     */
    private void saveSourceControl(ReadOnlySourceControl sourceControl, String filePath) {
        try {
            new JsonSourceControlStorage(Paths.get(filePath))
                    .saveSourceControl(sourceControl, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveSourceControl_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveSourceControl(new SourceControl(), null));
    }
}
