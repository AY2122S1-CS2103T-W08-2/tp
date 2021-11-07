package seedu.sourcecontrol.ui;

import static seedu.sourcecontrol.logic.commands.ShowCommand.Info;

import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.AssessmentStatistics;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.tag.Tag;

/**
 * A UI component that displays information of a {@code Student}.
 */
public class InfoDisplay extends UiPart<Region> {

    private static final String FXML = "InfoDisplay.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    private Student student;
    private Assessment assessment;
    private Group group;

    @FXML
    private Label name;
    @FXML
    private Label info1;
    @FXML
    private Label info2;
    @FXML
    private Label info3;
    @FXML
    private Label info4;
    @FXML
    private Label info5;

    public InfoDisplay() {
        super(FXML);
    }

    public void setInfo(Info info) {
        if (info.getStudent().isPresent()) {
            student = info.getStudent().get();
            setStudentInfo(student);
        }

        if (info.getAssessment().isPresent()) {
            assessment = info.getAssessment().get();
            setAssessmentInfo(assessment);
        }

        if (info.getGroup().isPresent()) {
            group = info.getGroup().get();
            setGroupInfo(group);
        }
    }

    /**
     * Displays info of a {@code student}.
     */
    public void setStudentInfo(Student student) {
        name.setText(student.getName().fullName);
        info1.setText("ID: " + student.getId().value);

        String groupsString = student.getGroups().stream()
                .map(Group::toString).collect(Collectors.joining(", "));
        info2.setText("Groups: " + groupsString);

        String tagsString = student.getTags().stream()
                .map(Tag::getTagName).collect(Collectors.joining(", "));
        info3.setText("Tags: " + tagsString);
    }

    /**
     * Displays info of an {@code assessment}.
     */
    public void setAssessmentInfo(Assessment assessment) {
        name.setText(assessment.getName());

        AssessmentStatistics statistics = new AssessmentStatistics(assessment);

        String min = reformatStats(statistics.getMin());
        String max = reformatStats(statistics.getMax());
        String median = reformatStats(statistics.getMedian());
        String mean = reformatStats(statistics.getMean());
        String percentile25 = reformatStats(statistics.getXPercentile(25));
        String percentile75 = reformatStats(statistics.getXPercentile(75));

        info1.setText("Grade range: " + min + " - " + max);
        info2.setText("Median: " + median);
        info3.setText("Mean: " + mean);
        info4.setText("25th percentile: " + percentile25);
        info5.setText("75th percentile: " + percentile75);
    }

    /**
     * Displays info of a {@code group}.
     */
    public void setGroupInfo(Group group) {
        name.setText(group.getName());

        info1.setText("Number of students: " + group.getStudents().size());
        info2.setText("List of students are displayed in the lower left window.");
    }

    /**
     * Clears any existing info.
     */
    public void clearInfo() {
        name.setText(null);
        info1.setText(null);
        info2.setText(null);
        info3.setText(null);
        info4.setText(null);
        info5.setText(null);
    }

    /**
     * Reformats numeric values to two decimal places.
     */
    private static String reformatStats(double stats) {
        return String.format("%.2f", stats);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof InfoDisplay)) {
            return false;
        }

        // state check
        InfoDisplay info = (InfoDisplay) other;
        return student.equals(info.student);
    }
}
