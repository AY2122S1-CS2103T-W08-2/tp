package seedu.sourcecontrol.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;
import static seedu.sourcecontrol.testutil.TypicalStudents.ALICE;
import static seedu.sourcecontrol.testutil.TypicalStudents.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.commons.core.GuiSettings;
import seedu.sourcecontrol.logic.parser.Alias;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.name.NameContainsKeywordsPredicate;
import seedu.sourcecontrol.testutil.SourceControlBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new SourceControl(), new SourceControl(modelManager.getSourceControl()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setSourceControlFilePath(Paths.get("source/control/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4, 0.5));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setSourceControlFilePath(Paths.get("new/source/control/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4, 0.5);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setSourceControlFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setSourceControlFilePath(null));
    }

    @Test
    public void setSourceControlFilePath_validPath_setsSourceControlFilePath() {
        Path path = Paths.get("source/control/file/path");
        modelManager.setSourceControlFilePath(path);
        assertEquals(path, modelManager.getSourceControlFilePath());
    }

    @Test
    public void hasStudent_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasStudent(null));
    }

    @Test
    public void hasStudent_studentNotInSourceControl_returnsFalse() {
        assertFalse(modelManager.hasStudent(ALICE));
    }

    @Test
    public void hasStudent_studentInSourceControl_returnsTrue() {
        modelManager.addStudent(ALICE);
        assertTrue(modelManager.hasStudent(ALICE));
    }

    @Test
    public void getFilteredStudentList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredStudentList().remove(0));
    }

    @Test
    public void setAndGetAliases_returnsEqualMap() {
        ModelManager modelManager = new ModelManager();
        Map<String, String> aliases = new HashMap<>();
        aliases.put("bye", "exit");
        modelManager.setAliases(aliases);
        assertEquals(modelManager.getAliases(), aliases);
    }

    @Test
    public void addAlias_getAlias_returnsExpectedMap() {
        ModelManager modelManager = new ModelManager();
        Alias alias = new Alias("bye", "exit");
        modelManager.addAlias(alias);

        Map<String, String> expectedAliases = new HashMap<>();
        expectedAliases.put("bye", "exit");
        assertEquals(modelManager.getAliases(), expectedAliases);
    }

    @Test
    public void removeAlias() {
        ModelManager modelManager = new ModelManager();
        Alias alias = new Alias("bye", "exit");
        modelManager.addAlias(alias);
        modelManager.removeAlias(alias.getAliasWord());
        assertEquals(modelManager.getAliases(), new HashMap<>());
    }

    @Test
    public void getGroup() {
        ModelManager modelManager = new ModelManager();
        Group added = new Group("T01A");
        modelManager.addGroup(added);
        assertSame(modelManager.getGroup(new Group("T01A")), added);
    }

    @Test
    public void getAssessment() {
        ModelManager modelManager = new ModelManager();
        Assessment added = new Assessment("P01");
        modelManager.addAssessment(added);
        assertSame(modelManager.getAssessment(new Assessment("P01")), added);
    }

    @Test
    public void equals() {
        SourceControl sourceControl = new SourceControlBuilder().withStudent(ALICE).withStudent(BENSON).build();
        SourceControl differentSourceControl = new SourceControl();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(sourceControl, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(sourceControl, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different sourceControl -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentSourceControl, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredStudentList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(sourceControl, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setSourceControlFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(sourceControl, differentUserPrefs)));
    }
}
