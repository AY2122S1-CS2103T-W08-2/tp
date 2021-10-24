package seedu.address.model.student;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javafx.scene.chart.Chart;
import seedu.address.commons.util.ChartUtil;

/**
 * Represents statistics about a student and the students' performance in each assessment.
 */
public class StudentStatistics {

    private static final String CHART_TITLE = "'s Results";
    private static final String CHART_X_AXIS_LABEL = "Assessments";
    private static final String CHART_Y_AXIS_LABEL = "Scores";

    private final Student student;
    private final Map<Assessment, Score> scoreMap;
//    private int numScores = 0;
//    private double sumOfScores = 0.0;

    /**
     * Constructs a {@code studentStatistics} with the specified {@code student}.
     */
    public StudentStatistics(Student student) {
        requireNonNull(student);
        requireNonNull(student.getScores());
        this.student = student;
        this.scoreMap = student.getScores();
    }

    /**
     * Returns a distribution of scores for the assessment, with the bins in their string representations.
     */
    private Map<String, Number> getScoreDistribution() {
        Map<String, Number> distribution = new TreeMap<>();
        scoreMap.forEach((assessment, score) -> distribution.put(assessment.getValue(), score.getNumericValue()));
        return distribution;
    }

    //TODO: now uses mean not median
    private Map<String, Number> getMedian(Assessment assessment) {
        AssessmentStatistics statistics = new AssessmentStatistics(assessment);
        Map<String, Number> distribution = new TreeMap<>();
        distribution.put(assessment.getValue(), statistics.getMean());
        return distribution;
    }


    private List<Map<String, Number>> getDataSet() {
        ArrayList<Map<String, Number>> dataSet = new ArrayList<>();
        dataSet.add(getScoreDistribution());
        scoreMap.forEach((assessment, score) -> dataSet.add(getMedian(assessment)));
        return dataSet;
    }

    /**
     * Returns a line chart representing the scores of student for each assessment.
     */
    public Chart toLineChart() {
        return ChartUtil.createLineChart(student.getName() + CHART_TITLE,
                CHART_X_AXIS_LABEL, CHART_Y_AXIS_LABEL, getDataSet());
    }
}
