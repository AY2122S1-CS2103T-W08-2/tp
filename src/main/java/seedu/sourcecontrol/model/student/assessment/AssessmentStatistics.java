package seedu.sourcecontrol.model.student.assessment;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.commons.util.AppUtil.checkArgument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javafx.scene.chart.Chart;
import seedu.sourcecontrol.commons.util.ChartUtil;

/**
 * Represents statistics about an assessment and the students' performance.
 */
public class AssessmentStatistics {
    public static final double DEFAULT_BIN_SIZE = 10.0;

    private static final String CHART_TITLE = "Cohort Performance for %1$s";
    private static final String CHART_X_AXIS_LABEL = "Scores";
    private static final String CHART_Y_AXIS_LABEL = "Number of Students";

    private final Assessment assessment;

    /**
     * Constructs a {@code AssessmentStatistics} with the specified {@code Assessment}.
     */
    public AssessmentStatistics(Assessment assessment) {
        requireNonNull(assessment);
        requireNonNull(assessment.scores);

        this.assessment = assessment;
    }

    /**
     * Creates a list of bins spanning from the minimum to maximum score, with each {@code Bin} having the specified
     * bin size.
     */
    public static List<Bin> createBins(double binSize) {
        checkArgument(binSize > Score.MIN_SCORE && binSize <= Score.MAX_SCORE);

        List<Bin> bins = new ArrayList<>();

        double binLowestValue = Score.MIN_SCORE;

        while (binLowestValue < Score.MAX_SCORE) {
            Score binLowestScore = new Score(String.valueOf(binLowestValue));
            double binHighestValue = Math.min(binLowestValue + binSize, Score.MAX_SCORE);
            Score binHighestScore = new Score(String.valueOf(binHighestValue));
            bins.add(new Bin(binLowestScore, binHighestScore));
            binLowestValue = binHighestValue;
        }

        return bins;
    }

    /**
     * Returns the {@code Bin} that the {@code Score} belongs in.
     */
    public static Bin getBinForScore(Collection<Bin> bins, Score score) {
        for (Bin b : bins) {
            if (b.includesScore(score)) {
                return b;
            }
        }

        // Should not happen since a Score must be between the minimum and maximum value (inclusive)
        assert false;

        return null;
    }

    /**
     * Adds the specified {@code Score} to its corresponding {@code Bin}.
     */
    public static void addScoreToBin(Map<Bin, Integer> binCounts, Score score) {
        Bin binForScore = getBinForScore(binCounts.keySet(), score);
        binCounts.put(binForScore, binCounts.get(binForScore) + 1);
    }

    /**
     * Returns a distribution of scores for the assessment, with the bins in their string representations.
     */
    public Map<String, Number> getScoreDistribution() {
        Map<Bin, Integer> binCounts = new HashMap<>();

        List<Bin> bins = createBins(DEFAULT_BIN_SIZE);

        for (Bin b : bins) {
            binCounts.put(b, 0);
        }

        for (Score score : assessment.scores.values()) {
            addScoreToBin(binCounts, score);
        }

        Map<String, Number> distribution = new TreeMap<>();
        binCounts.forEach((bin, count) -> distribution.put(bin.toString(), count));
        return distribution;
    }

    /**
     * Returns the minimum score for the {@code Assessment}.
     */
    public double getMin() {
        Collection<Score> scores = assessment.getScores().values();
        Optional<Double> min = scores.stream()
                .map(Score::getNumericValue)
                .min(Comparator.naturalOrder());
        return min.orElse(Score.MIN_SCORE);
    }

    /**
     * Returns the maximum score for the {@code Assessment}.
     */
    public double getMax() {
        Collection<Score> scores = assessment.getScores().values();
        Optional<Double> max = scores.stream()
                .map(Score::getNumericValue)
                .max(Comparator.naturalOrder());
        return max.orElse(Score.MIN_SCORE);
    }

    /**
     * Returns the median score for the {@code Assessment}.
     */
    public double getMedian() {
        Collection<Score> scores = assessment.getScores().values();
        List<Double> sorted = scores.stream()
                .map(Score::getNumericValue)
                .sorted().collect(Collectors.toList());

        if (sorted.isEmpty()) {
            return Score.MIN_SCORE;
        }

        long size = sorted.size();
        int midPos; // middle position of the sorted list
        if (size % 2 == 1) { // odd number of scores
            midPos = (int) ((size + 1) / 2.0 - 1);
            return sorted.get(midPos);
        } else { // even number of scores
            midPos = (int) (size / 2.0);
            return (sorted.get(midPos - 1)
                + sorted.get(midPos)) / 2.0;
        }
    }

    /**
     * Returns the mean score for the {@code Assessment}.
     */
    public double getMean() {
        int numScores = 0;
        double sumOfScores = 0.0;
        for (Score score : assessment.scores.values()) {
            numScores++;
            sumOfScores += score.getNumericValue();
        }
        return numScores == 0 ? Score.MIN_SCORE : sumOfScores / numScores;
    }

    /**
     * Returns the Xth percentile score for the {@code Assessment}.
     */
    public double getXPercentile(int x) {
        assert x > -1 && x < 101;

        Collection<Score> scores = assessment.getScores().values();
        List<Double> sorted = scores.stream()
                .map(Score::getNumericValue)
                .sorted().collect(Collectors.toList());

        if (sorted.isEmpty()) {
            return Score.MIN_SCORE;
        }

        long size = sorted.size();
        int xPercentilePos = (int) Math.ceil(x / 100.0 * size);
        return sorted.get(xPercentilePos - 1); // list indexing starts at 0
    }

    /**
     * Returns a histogram representing the scores for the assessment.
     */
    public Chart toHistogram() {
        return ChartUtil.createBarChart(String.format(CHART_TITLE, assessment.getName()),
                CHART_X_AXIS_LABEL, CHART_Y_AXIS_LABEL, getScoreDistribution());
    }

    /**
     * Represents a bin in the histogram.
     */
    public static class Bin {
        private final Score binMinimum;
        private final Score binMaximum;
        private final boolean maxIsInclusive;

        /**
         * Constructs a {@code Bin} spanning from the specified minimum to the specified maximum score.
         */
        public Bin(Score binMinimum, Score binMaximum) {
            this.binMinimum = binMinimum;
            this.binMaximum = binMaximum;
            maxIsInclusive = binMaximum.isMaxScore();
        }

        /**
         * Returns whether the specified {@code Score} is included in this bin.
         */
        public boolean includesScore(Score score) {
            return score.getNumericValue() >= binMinimum.getNumericValue()
                    && (maxIsInclusive
                    ? score.getNumericValue() <= binMaximum.getNumericValue()
                    : score.getNumericValue() < binMaximum.getNumericValue());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Bin otherBin = (Bin) o;
            return binMinimum.equals(otherBin.binMinimum) && binMaximum.equals(otherBin.binMaximum);
        }

        @Override
        public int hashCode() {
            return Objects.hash(binMinimum, binMaximum);
        }

        @Override
        public String toString() {
            return String.format("%.0f-%.0f", binMinimum.getNumericValue(), binMaximum.getNumericValue());
        }
    }
}
