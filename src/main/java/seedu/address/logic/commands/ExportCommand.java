package seedu.address.logic.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import seedu.address.commons.util.FileUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Assessment;
import seedu.address.model.student.Group;
import seedu.address.model.student.ID;
import seedu.address.model.student.Score;
import seedu.address.model.student.Student;
import seedu.address.model.tag.Tag;


public class ExportCommand extends Command {
    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports data from to a file. \n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Exported to file: %1$s";
    public static final String MESSAGE_FAILURE = "Failed to export to file. ";

    public static final String BASE_PATH = "sourceControl%1$s.csv";

    private int groupColumns;
    private int tagColumns;
    private final Path file;

    /**
     * Creates an ExportCommand to export data to a file.
     */
    public ExportCommand(Path file) {
        this.file = file;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        try {
            FileUtil.createIfMissing(file);
        } catch (IOException e) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        StringBuilder writeContent = new StringBuilder();
        writeContent.append("Name,Id,");

        List<Student> students = model.getAddressBook().getStudentList();
        appendGroupHeaders(writeContent, students);

        List<Assessment> assessments = model.getAddressBook().getAssessmentList();
        appendAssessmentHeaders(writeContent, assessments);

        appendTagHeaders(writeContent, students);

        replaceLastCharacterWithNewLine(writeContent);

        for (Student student : students) {
            appendStudentRow(writeContent, student, assessments);
        }

        try {
            FileUtil.writeToFile(file, writeContent.toString());
        } catch (IOException e) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, file.toString()));
    }

    private void appendGroupHeaders(StringBuilder writeContent, List<Student> students) {
        groupColumns = 0;
        for (Student student : students) {
            groupColumns = Math.max(groupColumns, student.getGroups().size());
        }

        for (int i = 1; i <= groupColumns; i++) {
            writeContent.append("Group ").append(i).append(",");
        }
    }

    private void appendAssessmentHeaders(StringBuilder writeContent, List<Assessment> assessments) {
        for (Assessment assessment : assessments) {
            writeContent.append(assessment.name).append(",");
        }
    }

    private void appendTagHeaders(StringBuilder writeContent, List<Student> students) {
        tagColumns = 0;
        for (Student student : students) {
            tagColumns = Math.max(tagColumns, student.getTags().size());
        }

        for (int i = 1; i <= tagColumns; i++) {
            writeContent.append("Tag ").append(i).append(",");
        }
    }

    private void appendStudentRow(StringBuilder writeContent, Student student, List<Assessment> assessments) {
        writeContent.append(student.getName().fullName).append(",");
        writeContent.append(student.getId().getValue()).append(",");

        List<Group> groups = student.getGroups();
        for (Group group : groups) {
            writeContent.append(group.getName()).append(",");
        }
        for (int i = groups.size(); i < groupColumns; i++) {
            writeContent.append(",");
        }

        for (Assessment assessment : assessments) {
            Map<ID, Score> scores = assessment.getScores();
            if (scores.containsKey(student.getId())) {
                writeContent.append(scores.get(student.getId()).getValue());
            }
            writeContent.append(",");
        }

        List<Tag> tags = List.copyOf(student.getTags());
        for (Tag tag : tags) {
            writeContent.append(tag.tagName).append(",");
        }
        for (int i = tags.size(); i < tagColumns; i++) {
            writeContent.append(",");
        }

        replaceLastCharacterWithNewLine(writeContent);
    }

    private void replaceLastCharacterWithNewLine(StringBuilder writeContent) {
        int length = writeContent.length();
        writeContent.replace(length - 1, length, "\n");
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ExportCommand
                && this.file.equals(((ExportCommand) other).file));
    }
}
