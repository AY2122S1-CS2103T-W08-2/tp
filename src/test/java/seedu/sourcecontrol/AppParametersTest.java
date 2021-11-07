package seedu.sourcecontrol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import javafx.application.Application;
import seedu.sourcecontrol.commons.util.FileUtil;

public class AppParametersTest {

    private final ParametersStub parametersStub = new ParametersStub();
    private final AppParameters expected = new AppParameters();

    @Test
    public void parse_validConfigPath_success() {
        parametersStub.namedParameters.put("config", "config.json");
        expected.setConfigPath(FileUtil.pathOf("config.json"));
        assertEquals(expected, AppParameters.parse(parametersStub));
    }

    @Test
    public void parse_nullConfigPath_success() {
        parametersStub.namedParameters.put("config", null);
        assertEquals(expected, AppParameters.parse(parametersStub));
    }

    @Test
    public void parse_invalidConfigPath_success() {
        parametersStub.namedParameters.put("config", "a\0");
        expected.setConfigPath(null);
        assertEquals(expected, AppParameters.parse(parametersStub));
    }

    @Test
    public void equals() {
        // same object
        assertEquals(expected, expected);
        // of another type
        assertNotEquals(expected, 1);
        // null
        assertNotEquals(expected, null);

        AppParameters ap1 = new AppParameters();
        AppParameters ap2 = new AppParameters();
        // config path both null
        assertEquals(ap1, ap2);

        ap1.setConfigPath(Path.of("hello.world"));
        // one config path null
        assertNotEquals(ap1, ap2);

        ap2.setConfigPath(Path.of("hello.world"));
        // same config path
        assertEquals(ap1, ap2);

        ap1.setConfigPath(Path.of("goodbye.world"));
        assertNotEquals(ap1, ap2);
    }

    @Test
    public void hashCode_sameAsConfigPath() {
        Path path = Path.of("hello.world");
        AppParameters ap1 = new AppParameters();
        ap1.setConfigPath(Path.of("hello.world"));
        assertEquals(ap1.hashCode(), path.hashCode());
    }

    private static class ParametersStub extends Application.Parameters {
        private Map<String, String> namedParameters = new HashMap<>();

        @Override
        public List<String> getRaw() {
            throw new AssertionError("should not be called");
        }

        @Override
        public List<String> getUnnamed() {
            throw new AssertionError("should not be called");
        }

        @Override
        public Map<String, String> getNamed() {
            return Collections.unmodifiableMap(namedParameters);
        }
    }
}
