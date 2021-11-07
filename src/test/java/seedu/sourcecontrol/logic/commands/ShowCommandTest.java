package seedu.sourcecontrol.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ASSESSMENT_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_TUTORIAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;
import static seedu.sourcecontrol.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.sourcecontrol.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.sourcecontrol.testutil.TypicalStudents.AMY;
import static seedu.sourcecontrol.testutil.TypicalStudents.getTypicalSourceControl;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.sourcecontrol.commons.core.index.Index;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ModelManager;
import seedu.sourcecontrol.model.UserPrefs;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.id.IdContainsKeywordsPredicate;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.model.student.name.NameEqualsPredicate;
import seedu.sourcecontrol.testutil.AssessmentBuilder;
import seedu.sourcecontrol.testutil.GroupBuilder;


/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code ShowCommand}.
 */
public class ShowCommandTest {

    @TempDir
    public Path testFolder;

    private final Model model = new ModelManager(getTypicalSourceControl(), new UserPrefs());

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void constructor_nullFirstArg_throwsNullPointerException() {
        Path path = getTempFilePath("error.csv");
        assertThrows(NullPointerException.class, () ->
            new ShowCommand((Name) null, path));

        assertThrows(NullPointerException.class, () ->
            new ShowCommand((Index) null, path));

        assertThrows(NullPointerException.class, () ->
            new ShowCommand((Id) null, path));

        assertThrows(NullPointerException.class, () ->
            new ShowCommand((Assessment) null, path));

        assertThrows(NullPointerException.class, () ->
            new ShowCommand((Group) null, path));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        ShowCommand showCommand = new ShowCommand(outOfBoundIndex, null);

        assertCommandFailure(showCommand, model,
                String.format(MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX, model.getFilteredStudentList().size()));
    }

    @Test
    public void execute_invalidName_throwsCommandException() {
        // AMY does not exist in typicalSourceControl
        Name name = AMY.getName();
        String studentName = name.toString();

        ShowCommand showCommand = new ShowCommand(name, null);

        Predicate<Student> predicate = new NameEqualsPredicate(studentName);
        model.updateFilteredStudentList(predicate);

        assertCommandFailure(showCommand, model, ShowCommand.MESSAGE_NONEXISTENT_STUDENT);
    }

    @Test
    public void execute_invalidId_throwsCommandException() {
        // AMY does not exist in typicalSourceControl
        Id id = AMY.getId();
        String studentId = id.value;

        ShowCommand showCommand = new ShowCommand(id, null);

        Predicate<Student> predicate = new IdContainsKeywordsPredicate(List.of(studentId));
        model.updateFilteredStudentList(predicate);

        assertCommandFailure(showCommand, model, ShowCommand.MESSAGE_NONEXISTENT_STUDENT);
    }

    @Test
    public void execute_invalidGroup_throwsCommandException() {
        Group group = new GroupBuilder().withName(VALID_GROUP_TUTORIAL).build();

        ShowCommand showCommand = new ShowCommand(group, null);

        assertCommandFailure(showCommand, model, ShowCommand.MESSAGE_NONEXISTENT_GROUP);
    }

    @Test
    public void execute_invalidAssessment_throwsCommandException() {
        Assessment assessment = new AssessmentBuilder().withValue(VALID_ASSESSMENT_AMY).build();

        ShowCommand showCommand = new ShowCommand(assessment, null);

        assertCommandFailure(showCommand, model, ShowCommand.MESSAGE_NONEXISTENT_ASSESSMENT);
    }

    @Test
    public void equals() {
        ShowCommand showFirstCommand = new ShowCommand(INDEX_FIRST_STUDENT, Path.of("abc.csv"));
        ShowCommand showSecondCommand = new ShowCommand(INDEX_FIRST_STUDENT, Path.of("abc.csv"));

        // same object -> returns true
        assertTrue(showFirstCommand.equals(showFirstCommand));

        // same values -> returns true
        assertTrue(showFirstCommand.equals(showSecondCommand));

        // different types -> returns false
        assertFalse(showFirstCommand.equals(1));

        // null -> returns false
        assertFalse(showFirstCommand.equals(null));

        // different ID -> returns false
        showSecondCommand = new ShowCommand(INDEX_SECOND_STUDENT, Path.of("abc.csv"));
        assertFalse(showFirstCommand.equals(showSecondCommand));

        //diff types of arguments -> returns false
        showSecondCommand = new ShowCommand(new Name(VALID_NAME_AMY), Path.of("abc.csv"));
        assertFalse(showFirstCommand.equals(showSecondCommand));
    }
}
