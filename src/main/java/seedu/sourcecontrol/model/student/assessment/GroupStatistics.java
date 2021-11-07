package seedu.sourcecontrol.model.student.assessment;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.scene.chart.Chart;
import seedu.sourcecontrol.commons.util.ChartUtil;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;

/**
 * Represents statistics about a student and the students' performance in each assessment.
 */
public class GroupStatistics {

    private static final String CHART_TITLE = "Performance of %1$s";
    private static final String CHART_X_AXIS_LABEL = "Assessments";
    private static final String CHART_Y_AXIS_LABEL = "Scores";

    private final Group group;
    private final List<Id> studentList;
    private final List<Assessment> assessmentList;

    /**
     * Constructs a {@code GroupStatistics} with the specified {@code group}.
     */
    public GroupStatistics(Group group, List<Assessment> assessmentList) {
        requireNonNull(group);
        requireNonNull(group.getStudents());
        requireNonNull(assessmentList);
        this.group = group;
        this.studentList = group.getStudents();
        this.assessmentList = assessmentList;
    }

    /**
     * Returns true if more than one student in group is graded in {@code assessment}.
     */
    public boolean isGraded(Assessment assessment) {
        for (Id id : studentList) {
            if (assessment.isGraded(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns median score of students in group who are graded in {@code assessment}.
     */
    public double getMedian(Assessment assessment) {
        int count = 0;
        ArrayList<Score> scores = new ArrayList<>();
        for (Id id : studentList) {
            if (!assessment.isGraded(id)) {
                continue;
            }
            scores.add(assessment.getScores().get(id));
            count++;
        }

        double median;

        List<Double> sorted = scores.stream()
                .map(Score::getNumericValue)
                .sorted().collect(Collectors.toList());
        int midPos = count / 2; // middle position of the sorted list
        if (count % 2 == 1) { // odd number of scores
            median = sorted.get(midPos);
        } else { // even number of scores
            median = (sorted.get(midPos - 1)
                    + sorted.get(midPos)) / 2.0;
        }

        return median;
    }

    /**
     * Returns a distribution of scores for the assessment.
     *
     * @return array of {@code Map<String, Number>} where
     * the first element contains the group median distribution,
     * the second element contains the cohort mean distribution,
     * and the third element contains the cohort median distribution.
     */
    public Map<String, Number>[] getDataSet() {
        Map<String, Number> groupMedian = new LinkedHashMap<>();
        Map<String, Number> cohortMean = new LinkedHashMap<>();
        Map<String, Number> cohortMedian = new LinkedHashMap<>();

        for (Assessment assessment: assessmentList) {
            if (!isGraded(assessment)) {
                continue;
            }
            // group median
            groupMedian.put(assessment.getName(), getMedian(assessment));

            // cohort mean and median
            AssessmentStatistics statistics = new AssessmentStatistics(assessment);
            cohortMean.put(assessment.getName(), statistics.getMean());
            cohortMedian.put(assessment.getName(), statistics.getMedian());
        }

        @SuppressWarnings("unchecked")
        Map<String, Number>[] dataSet = new Map[]{groupMedian, cohortMean, cohortMedian};
        return dataSet;
    }

    /**
     * Returns a line chart representing the scores of student for each assessment.
     */
    public Chart toLineChart() {
        Map<String, Number>[] dataSet = getDataSet();
        return ChartUtil.createLineChart(String.format(CHART_TITLE, group.getName()),
                CHART_X_AXIS_LABEL, CHART_Y_AXIS_LABEL, "group median  ", dataSet[0], dataSet[1], dataSet[2]);
    }
}
