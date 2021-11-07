package seedu.sourcecontrol.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;


public class FileUtilTest {

    @Test
    public void isValidPath() {
        // valid path
        assertTrue(FileUtil.isValidPath("valid/file/path"));

        // invalid path
        assertFalse(FileUtil.isValidPath("a\0"));

        // null path -> throws NullPointerException
        assertThrows(NullPointerException.class, () -> FileUtil.isValidPath(null));
    }

    @Test
    public void getRelativePathString() {
        Path path = FileUtil.pathOf("hello.world");
        assertEquals(FileUtil.getRelativePathString(path), Path.of(".").resolve(Path.of("hello.world")).toString());
    }

}
