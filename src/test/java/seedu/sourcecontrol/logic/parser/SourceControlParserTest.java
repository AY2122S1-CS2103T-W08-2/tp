package seedu.sourcecontrol.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ASSESSMENT_COUNT_DESC_TYPICAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.ASSESSMENT_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.FILE_DESC_VALID_FILE;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.GROUP_COUNT_DESC_TYPICAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.GROUP_DESC_TUTORIAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.SCORE_DESC_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.TAG_COUNT_DESC_TYPICAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ASSESSMENT_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_TUTORIAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_SCORE_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TYPICAL_STUDENTS_CSV_PATH;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TYPICAL_STUDENTS_GROUP_COUNT;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TYPICAL_STUDENTS_TAG_COUNT;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_COMMAND;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;
import static seedu.sourcecontrol.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.logic.commands.AddAllocCommand;
import seedu.sourcecontrol.logic.commands.AddAllocCommand.AllocDescriptor;
import seedu.sourcecontrol.logic.commands.AddAssessmentCommand;
import seedu.sourcecontrol.logic.commands.AddGroupCommand;
import seedu.sourcecontrol.logic.commands.AddScoreCommand;
import seedu.sourcecontrol.logic.commands.AddStudentCommand;
import seedu.sourcecontrol.logic.commands.AliasCommand;
import seedu.sourcecontrol.logic.commands.ClearCommand;
import seedu.sourcecontrol.logic.commands.DeleteCommand;
import seedu.sourcecontrol.logic.commands.EditCommand;
import seedu.sourcecontrol.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.sourcecontrol.logic.commands.ExitCommand;
import seedu.sourcecontrol.logic.commands.ExportCommand;
import seedu.sourcecontrol.logic.commands.HelpCommand;
import seedu.sourcecontrol.logic.commands.ImportCommand;
import seedu.sourcecontrol.logic.commands.ListCommand;
import seedu.sourcecontrol.logic.commands.SearchCommand;
import seedu.sourcecontrol.logic.commands.ShowCommand;
import seedu.sourcecontrol.logic.parser.exceptions.ParseException;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.name.NameContainsKeywordsPredicate;
import seedu.sourcecontrol.testutil.AllocDescriptorBuilder;
import seedu.sourcecontrol.testutil.AssessmentBuilder;
import seedu.sourcecontrol.testutil.EditStudentDescriptorBuilder;
import seedu.sourcecontrol.testutil.GroupBuilder;
import seedu.sourcecontrol.testutil.ScoreDescriptorBuilder;
import seedu.sourcecontrol.testutil.StudentBuilder;
import seedu.sourcecontrol.testutil.StudentUtil;

public class SourceControlParserTest {

    private final SourceControlParser parser = new SourceControlParser();

    @Test
    public void parseCommand_add() throws Exception {
        Student student = new StudentBuilder().build();
        AddStudentCommand command = (AddStudentCommand) parser.parseCommand(StudentUtil.getAddCommand(student));
        assertEquals(new AddStudentCommand(student), command);
    }

    @Test
    public void parseCommand_addGroup() throws Exception {
        Group group = new GroupBuilder().withName(VALID_GROUP_TUTORIAL).build();
        AllocDescriptor descriptor = new AllocDescriptorBuilder()
                .withGroup(VALID_GROUP_TUTORIAL)
                .withName(VALID_NAME_AMY).build();
        AddGroupCommand command = (AddGroupCommand) parser.parseCommand(AddGroupCommand.COMMAND_WORD + " "
                + GROUP_DESC_TUTORIAL + NAME_DESC_AMY);
        assertEquals(new AddGroupCommand(group, List.of(descriptor)), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_STUDENT.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_STUDENT), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Student student = new StudentBuilder().build();
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder(student).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_STUDENT.getOneBased() + " " + StudentUtil.getEditStudentDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_STUDENT, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_search() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        SearchCommand command = (SearchCommand) parser.parseCommand(
                SearchCommand.COMMAND_WORD + " -n " + String.join(" ", keywords));
        assertEquals(new SearchCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_import() throws Exception {
        ImportCommand command = (ImportCommand) parser.parseCommand(
                ImportCommand.COMMAND_WORD
                + FILE_DESC_VALID_FILE
                + GROUP_COUNT_DESC_TYPICAL
                + ASSESSMENT_COUNT_DESC_TYPICAL
                + TAG_COUNT_DESC_TYPICAL);

        assertEquals(new ImportCommand(
                VALID_TYPICAL_STUDENTS_GROUP_COUNT,
                VALID_TYPICAL_STUDENTS_ASSESSMENT_COUNT,
                VALID_TYPICAL_STUDENTS_TAG_COUNT,
                ParserUtil.parsePath(VALID_TYPICAL_STUDENTS_CSV_PATH, ".csv")), command);
    }

    @Test
    public void parseCommand_export() throws Exception {
        assertTrue(parser.parseCommand(ExportCommand.COMMAND_WORD) instanceof ExportCommand);
        assertTrue(parser.parseCommand(ExportCommand.COMMAND_WORD + " 3") instanceof ExportCommand);
    }

    @Test
    public void parseCommand_alias() throws Exception {
        assertTrue(parser.parseCommand(
                AliasCommand.COMMAND_WORD + " "
                + PREFIX_COMMAND + ExitCommand.COMMAND_WORD + " "
                + PREFIX_ALIAS + "aliasWord")
                instanceof AliasCommand);
    }

    @Test
    public void parseCommand_show() throws Exception {
        assertTrue(parser.parseCommand(
                ShowCommand.COMMAND_WORD
                        + ASSESSMENT_DESC_AMY
                        + FILE_DESC_VALID_FILE) instanceof ShowCommand);
    }

    @Test
    public void parseCommand_addAlloc() throws Exception {
        AllocDescriptor descriptor = new AllocDescriptorBuilder()
                .withGroup(VALID_GROUP_TUTORIAL)
                .withName(VALID_NAME_AMY)
                .build();

        AddAllocCommand command = (AddAllocCommand) parser.parseCommand(
                AddAllocCommand.COMMAND_WORD
                        + GROUP_DESC_TUTORIAL
                        + NAME_DESC_AMY);

        assertEquals(new AddAllocCommand(descriptor),
                command);
    }

    @Test
    public void parseCommand_addAssessment() throws Exception {
        Assessment simpleAssessment = new AssessmentBuilder()
                .withValue(VALID_ASSESSMENT_AMY).build();

        AddAssessmentCommand command = (AddAssessmentCommand) parser.parseCommand(
                AddAssessmentCommand.COMMAND_WORD
                        + ASSESSMENT_DESC_AMY);

        assertEquals(new AddAssessmentCommand(simpleAssessment),
                command);
    }

    @Test
    public void parseCommand_addScore() throws Exception {
        AddScoreCommand command = (AddScoreCommand) parser.parseCommand(
                AddScoreCommand.COMMAND_WORD
                        + ASSESSMENT_DESC_AMY
                        + NAME_DESC_AMY
                        + SCORE_DESC_AMY);

        AddScoreCommand.ScoreDescriptor scoreDescriptor = new ScoreDescriptorBuilder()
                .withAssessment(VALID_ASSESSMENT_AMY)
                .withName(VALID_NAME_AMY)
                .withScore(VALID_SCORE_AMY).build();

        assertEquals(new AddScoreCommand(scoreDescriptor),
                command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void addAlias() throws Exception {
        SourceControlParser parser = new SourceControlParser();
        Alias alias = new Alias("bye", ExitCommand.COMMAND_WORD);
        parser.addAlias(alias);

        assertTrue(parser.parseCommand("bye") instanceof ExitCommand);
    }

    @Test
    public void removeAlias() throws Exception {
        SourceControlParser parser = new SourceControlParser();
        Alias alias = new Alias("bye", ExitCommand.COMMAND_WORD);
        parser.addAlias(alias);

        assertTrue(parser.parseCommand("bye") instanceof ExitCommand);
        parser.removeAlias(alias.getAliasWord());
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("bye"));
    }

    @Test
    public void getAlias() {
        SourceControlParser parser = new SourceControlParser();
        Alias alias = new Alias("bye", ExitCommand.COMMAND_WORD);
        parser.addAlias(alias);

        assertTrue(parser.getAlias("bye").isPresent());
        assertSame(parser.getAlias("bye").get(), alias);
    }

    @Test
    public void equals() {
        // same object
        SourceControlParser parser1 = new SourceControlParser();
        assertEquals(parser1, parser1);

        // same new object
        SourceControlParser parser2 = new SourceControlParser();
        assertEquals(parser1, parser2);

        // different aliases
        parser1.addAlias(new Alias("bye", ExitCommand.COMMAND_WORD));
        assertNotEquals(parser1, parser2);

        // same aliases
        parser2.addAlias(new Alias("bye", ExitCommand.COMMAND_WORD));
        assertEquals(parser1, parser2);

        // null
        assertNotEquals(parser1, null);

        // different types
        assertNotEquals(parser1, 5);
    }
}
