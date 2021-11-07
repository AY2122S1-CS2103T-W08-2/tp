package seedu.sourcecontrol.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.commons.exceptions.IllegalValueException;
import seedu.sourcecontrol.commons.util.JsonUtil;
import seedu.sourcecontrol.model.SourceControl;
import seedu.sourcecontrol.testutil.TypicalStudents;

public class JsonSerializableSourceControlTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableSourceControlTest");
    private static final Path TYPICAL_STUDENTS_FILE = TEST_DATA_FOLDER.resolve("typicalStudentsSourceControl.json");
    private static final Path INVALID_STUDENT_FILE = TEST_DATA_FOLDER.resolve("invalidStudentSourceControl.json");
    private static final Path DUPLICATE_STUDENT_FILE = TEST_DATA_FOLDER.resolve("duplicateStudentSourceControl.json");
    private static final Path DUPLICATE_GROUP_FILE = TEST_DATA_FOLDER.resolve("duplicateGroupSourceControl.json");
    private static final Path DUPLICATE_ASSESSMENT_FILE =
            TEST_DATA_FOLDER.resolve("duplicateAssessmentSourceControl.json");

    @Test
    public void toModelType_typicalStudentsFile_success() throws Exception {
        JsonSerializableSourceControl dataFromFile = JsonUtil.readJsonFile(TYPICAL_STUDENTS_FILE,
                JsonSerializableSourceControl.class).get();
        SourceControl sourceControlFromFile = dataFromFile.toModelType();
        SourceControl typicalStudentsSourceControl = TypicalStudents.getTypicalSourceControl();
        assertEquals(sourceControlFromFile, typicalStudentsSourceControl);
    }

    @Test
    public void toModelType_invalidStudentFile_throwsIllegalValueException() throws Exception {
        JsonSerializableSourceControl dataFromFile = JsonUtil.readJsonFile(INVALID_STUDENT_FILE,
                JsonSerializableSourceControl.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateStudents_throwsIllegalValueException() throws Exception {
        JsonSerializableSourceControl dataFromFile = JsonUtil.readJsonFile(DUPLICATE_STUDENT_FILE,
                JsonSerializableSourceControl.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableSourceControl.MESSAGE_DUPLICATE_STUDENT,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateGroups_throwsIllegalValueException() throws Exception {
        JsonSerializableSourceControl dataFromFile = JsonUtil.readJsonFile(DUPLICATE_GROUP_FILE,
                JsonSerializableSourceControl.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableSourceControl.MESSAGE_DUPLICATE_GROUP,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateAssessments_throwsIllegalValueException() throws Exception {
        JsonSerializableSourceControl dataFromFile = JsonUtil.readJsonFile(DUPLICATE_ASSESSMENT_FILE,
                JsonSerializableSourceControl.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableSourceControl.MESSAGE_DUPLICATE_ASSESSMENT,
                dataFromFile::toModelType);
    }

}
