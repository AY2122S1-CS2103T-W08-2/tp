package seedu.sourcecontrol.logic.commands;

import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ASSESSMENT;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_FILE;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_TAG;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import seedu.sourcecontrol.commons.util.FileUtil;
import seedu.sourcecontrol.logic.commands.exceptions.CommandException;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.SourceControl;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.Score;
import seedu.sourcecontrol.model.student.exceptions.DuplicateStudentException;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.model.student.tag.Tag;


public class ImportCommand extends Command {
    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports data from a file. \n"
            + "Parameters: "
            + PREFIX_FILE + "<file_path> "
            + "[" + PREFIX_GROUP + "<number_of_group_columns>] "
            + "[" + PREFIX_ASSESSMENT + "<number_of_assessment_columns>] "
            + "[" + PREFIX_TAG + "<number_of_tag_columns>]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_FILE + "data.csv "
            + PREFIX_GROUP + "2 "
            + PREFIX_ASSESSMENT + "10 "
            + PREFIX_TAG + "3";


    public static final String MESSAGE_SUCCESS = "Imported all students. ";
    public static final String MESSAGE_INVALID_FILE = "Failed to read from the file. ";
    public static final String MESSAGE_INVALID_NUMBER = "Number of columns should be a non-negative integer. ";
    public static final String MESSAGE_OUT_OF_BOUNDS = "Reached unexpected end of line while reading from file. ";
    public static final String MESSAGE_DUPLICATE_ASSESSMENT = "Duplicate assessment found in file. ";
    public static final String MESSAGE_DUPLICATE_ID = "Duplicate student ID found in file. ";


    private final int groupCount;
    private final int assessmentCount;
    private final int tagCount;
    private final Path file;

    /**
     * Creates an ImportCommand to import data from the given {@code file}
     */
    public ImportCommand(int groupCount, int assessmentCount, int tagCount, Path file) {
        this.groupCount = groupCount;
        this.assessmentCount = assessmentCount;
        this.tagCount = tagCount;
        this.file = file;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        String fileContents;
        try {
            fileContents = FileUtil.readFromFile(file);
        } catch (IOException e) {
            throw new CommandException(MESSAGE_INVALID_FILE);
        }

        SourceControl newAb = new SourceControl();
        List<Assessment> assessments = new ArrayList<>();

        String[] lines = fileContents.split("\n");
        String[] headers = lines[0].split(",", -1);
        for (int i = 0; i < assessmentCount; i++) {
            String assessmentName = readValue(headers, groupCount + 2 + i);
            Assessment assessment = makeAssessment(assessmentName);
            if (assessments.contains(assessment)) {
                throw new CommandException(MESSAGE_DUPLICATE_ASSESSMENT);
            }
            assessments.add(assessment);
            newAb.addAssessment(assessment);
        }

        for (int i = 1; i < lines.length; i++) {
            try {
                newAb.addStudent(readStudentFromRow(lines[i], assessments, newAb.getGroupList()));
            } catch (DuplicateStudentException e) {
                throw new CommandException(MESSAGE_DUPLICATE_ID);
            }
        }

        model.setSourceControl(newAb);

        return new CommandResult(MESSAGE_SUCCESS);
    }

    private Student readStudentFromRow(String row,
                                       List<Assessment> assessments,
                                       List<Group> groupList) throws CommandException {
        String[] values = row.split(",", -1);
        Name name = makeName(readValue(values, 0));
        Id id = makeId(readValue(values, 1));
        int readingColumn = 2;

        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < groupCount; i++, readingColumn++) {
            String groupName = readValue(values, readingColumn);
            if (!groupName.isEmpty()) {
                Group group = makeGroup(groupName);
                // if the group already exists in the group list, use the existing group.
                // otherwise, use the new group.
                Group toAdd = groupList.stream().filter(grp -> grp.equals(group))
                        .findFirst().orElse(group);
                group.addStudent(id);
                groups.add(toAdd);
            }
        }

        Map<Assessment, Score> scores = new LinkedHashMap<>();
        for (int i = 0; i < assessmentCount; i++, readingColumn++) {
            String assessmentScore = readValue(values, readingColumn);
            if (!assessmentScore.isEmpty()) {
                Score score = makeScore(assessmentScore);
                scores.put(assessments.get(i), score);
                assessments.get(i).setScore(id, score);
            }
        }

        Set<Tag> tags = new HashSet<>();
        for (int i = 0; i < tagCount; i++, readingColumn++) {
            String tagName = readValue(values, readingColumn);
            if (!tagName.isEmpty()) {
                tags.add(makeTag(tagName));
            }
        }

        return new Student(name, id, groups, scores, tags);
    }

    private String readValue(String[] row, int column) throws CommandException {
        if (column >= row.length) {
            // avoid array out of bounds error
            throw new CommandException(MESSAGE_OUT_OF_BOUNDS);
        }
        return row[column].trim();
    }

    private Name makeName(String name) throws CommandException {
        if (!Name.isValidName(name)) {
            throw new CommandException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(name);
    }

    private Id makeId(String id) throws CommandException {
        if (!Id.isValidID(id)) {
            throw new CommandException(Id.MESSAGE_CONSTRAINTS);
        }
        return new Id(id);
    }

    private Group makeGroup(String groupName) throws CommandException {
        if (!Group.isValidGroup(groupName)) {
            throw new CommandException(Group.MESSAGE_CONSTRAINTS);
        }
        return new Group(groupName);
    }

    private Score makeScore(String score) throws CommandException {
        if (!Score.isValidScore(score)) {
            throw new CommandException(Score.MESSAGE_CONSTRAINTS);
        }
        return new Score(score);
    }

    private Assessment makeAssessment(String assessmentName) throws CommandException {
        if (!Assessment.isValidAssessment(assessmentName)) {
            throw new CommandException(Assessment.MESSAGE_CONSTRAINTS);
        }
        return new Assessment(assessmentName);
    }

    private Tag makeTag(String tagName) throws CommandException {
        if (!Tag.isValidTagName(tagName)) {
            throw new CommandException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(tagName);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ImportCommand
                && this.file.equals(((ImportCommand) other).file)
                && this.groupCount == ((ImportCommand) other).groupCount
                && this.assessmentCount == ((ImportCommand) other).assessmentCount
                && this.tagCount == ((ImportCommand) other).tagCount);
    }
}

