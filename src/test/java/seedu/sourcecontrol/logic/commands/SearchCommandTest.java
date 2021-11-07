package seedu.sourcecontrol.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.sourcecontrol.testutil.TypicalStudents.ALICE;
import static seedu.sourcecontrol.testutil.TypicalStudents.BENSON;
import static seedu.sourcecontrol.testutil.TypicalStudents.CARL;
import static seedu.sourcecontrol.testutil.TypicalStudents.DANIEL;
import static seedu.sourcecontrol.testutil.TypicalStudents.ELLE;
import static seedu.sourcecontrol.testutil.TypicalStudents.FIONA;
import static seedu.sourcecontrol.testutil.TypicalStudents.getTypicalSourceControl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ModelManager;
import seedu.sourcecontrol.model.UserPrefs;
import seedu.sourcecontrol.model.student.group.GroupContainsKeywordsPredicate;
import seedu.sourcecontrol.model.student.id.IdContainsKeywordsPredicate;
import seedu.sourcecontrol.model.student.name.NameContainsKeywordsPredicate;
import seedu.sourcecontrol.model.student.tag.TagContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code SearchCommand}.
 */
public class SearchCommandTest {
    private Model model = new ModelManager(getTypicalSourceControl(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalSourceControl(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        SearchCommand findFirstCommand = new SearchCommand(firstPredicate);
        SearchCommand findSecondCommand = new SearchCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        SearchCommand findFirstCommandCopy = new SearchCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different student -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroNameKeywords_noStudentsFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, "0 students");
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(parseInput(" "));
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredStudentList());
    }

    @Test
    public void execute_multipleNameKeywords_multipleStudentsFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, "3 students");
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(parseInput("Kurz Elle Kunz"));
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredStudentList());
    }

    @Test
    public void execute_zeroIdKeywords_noStudentsFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, "0 students");
        IdContainsKeywordsPredicate predicate = new IdContainsKeywordsPredicate(parseInput(" "));
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredStudentList());
    }

    @Test
    public void execute_multipleIdKeywords_multipleStudentsFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, "3 students");
        IdContainsKeywordsPredicate predicate =
                new IdContainsKeywordsPredicate(parseInput("E0538201 E0582305 E0537266"));
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredStudentList());
    }

    @Test
    public void execute_zeroGroupKeywords_noStudentsFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, "0 students");
        GroupContainsKeywordsPredicate predicate = new GroupContainsKeywordsPredicate(parseInput(" "));
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredStudentList());
    }

    @Test
    public void execute_multipleGroupKeywords_multipleStudentsFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, "3 students");
        GroupContainsKeywordsPredicate predicate = new GroupContainsKeywordsPredicate(parseInput("R03A R01C"));
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, CARL, FIONA), model.getFilteredStudentList());
    }

    @Test
    public void execute_zeroTagKeywords_noStudentsFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, "0 students");
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(parseInput(" "));
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredStudentList());
    }

    @Test
    public void execute_singleTagKeyword_multipleStudentsFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, "3 students");
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(parseInput("friends"));
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getFilteredStudentList());
    }

    /**
     * Parses {@code userInput} into a {@code List<String>}.
     */
    private List<String> parseInput(String userInput) {
        return Arrays.asList(userInput.split("\\s+"));
    }

}
