package seedu.sourcecontrol.model.student.assessment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.sourcecontrol.model.student.assessment.AssessmentStatistics.Bin;
import static seedu.sourcecontrol.model.student.assessment.AssessmentStatistics.addScoreToBin;
import static seedu.sourcecontrol.model.student.assessment.AssessmentStatistics.createBins;
import static seedu.sourcecontrol.model.student.assessment.AssessmentStatistics.getBinForScore;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.model.student.id.Id;

public class AssessmentStatisticsTest {
    private final Assessment assessment = new Assessment("P01");

    private final Map<Id, Score> scores = Map.of(
            new Id("E0000001"), new Score("5"),
            new Id("E0000002"), new Score("13"),
            new Id("E0000003"), new Score("46"),
            new Id("E0000004"), new Score("48"),
            new Id("E0000005"), new Score("55"),
            new Id("E0000006"), new Score("66"),
            new Id("E0000007"), new Score("70"),
            new Id("E0000008"), new Score("78"),
            new Id("E0000009"), new Score("88"),
            new Id("E0000010"), new Score("100")
    );

    private final List<Bin> bins = Arrays.asList(
            new Bin(new Score("0"), new Score("10")),
            new Bin(new Score("10"), new Score("20")),
            new Bin(new Score("20"), new Score("30")),
            new Bin(new Score("30"), new Score("40")),
            new Bin(new Score("40"), new Score("50")),
            new Bin(new Score("50"), new Score("60")),
            new Bin(new Score("60"), new Score("70")),
            new Bin(new Score("70"), new Score("80")),
            new Bin(new Score("80"), new Score("90")),
            new Bin(new Score("90"), new Score("100"))
    );

    @Test
    public void createBins_invalidBinSize_throwsException() {
        // Negative bin size
        assertThrows(IllegalArgumentException.class, () -> createBins(-1));

        // Zero bin size
        assertThrows(IllegalArgumentException.class, () -> createBins(0));

        // Bin size greater than max score
        assertThrows(IllegalArgumentException.class, () -> createBins(Score.MAX_SCORE + 0.01));
    }

    @Test
    public void createBins_normalBinSize_success() {
        assertEquals(bins, createBins(10));
    }

    @Test
    public void getBinForScore_middleBin_returnsCorrectBin() {
        Bin binBetween30and40 = new Bin(new Score("30"), new Score("40"));

        assertEquals(binBetween30and40, getBinForScore(bins, new Score("30")));
        assertEquals(binBetween30and40, getBinForScore(bins, new Score("35")));
        assertEquals(binBetween30and40, getBinForScore(bins, new Score("39.99")));
    }

    @Test
    public void getBinForScore_lastBin_returnsCorrectBin() {
        Bin lastBin = new Bin(new Score("90"), new Score("100"));

        assertEquals(lastBin, getBinForScore(bins, new Score("90")));
        assertEquals(lastBin, getBinForScore(bins, new Score("95")));
        assertEquals(lastBin, getBinForScore(bins, new Score("100")));
    }

    @Test
    public void addScoreToBin_scoreAddedToCorrectBin() {
        Map<Bin, Integer> binCounts = new HashMap<>();
        for (Bin b : bins) {
            binCounts.put(b, 0);
        }

        addScoreToBin(binCounts, new Score("35"));
        Bin correctBin = new Bin(new Score("30"), new Score("40"));

        for (Map.Entry<Bin, Integer> entry : binCounts.entrySet()) {
            if (entry.getKey().equals(correctBin)) {
                assertEquals(1, entry.getValue());
            } else {
                assertEquals(0, entry.getValue());
            }
        }
    }

    @Test
    public void getScoreDistribution_returnsCorrectDistribution() {
        assessment.setScores(scores);
        AssessmentStatistics statistics = new AssessmentStatistics(assessment);

        Map<String, Integer> expected = Map.of(
                "0-10", 1,
                "10-20", 1,
                "20-30", 0,
                "30-40", 0,
                "40-50", 2,
                "50-60", 1,
                "60-70", 1,
                "70-80", 2,
                "80-90", 1,
                "90-100", 1
        );

        assertEquals(expected, statistics.getScoreDistribution());
    }

    @Test
    public void getMin_assessmentWithScores_returnsMin() {
        assessment.setScores(scores);
        AssessmentStatistics statistics = new AssessmentStatistics(assessment);

        assertEquals(5, statistics.getMin());
    }

    @Test
    public void getMin_assessmentWithoutScores_returnsZeroScore() {
        assessment.setScores(new HashMap<>());
        AssessmentStatistics statistics = new AssessmentStatistics(assessment);

        assertEquals(Score.MIN_SCORE, statistics.getMin());
    }

    @Test
    public void getMax_assessmentWithScores_returnsMax() {
        assessment.setScores(scores);
        AssessmentStatistics statistics = new AssessmentStatistics(assessment);

        assertEquals(100, statistics.getMax());
    }

    @Test
    public void getMax_assessmentWithoutScores_returnsZeroScore() {
        assessment.setScores(new HashMap<>());
        AssessmentStatistics statistics = new AssessmentStatistics(assessment);

        assertEquals(Score.MIN_SCORE, statistics.getMax());
    }

    @Test
    public void getMedian_assessmentWithEvenScores_returnsAverageOfMiddleTwoScores() {
        assessment.setScores(scores);
        AssessmentStatistics statistics = new AssessmentStatistics(assessment);

        assertEquals(60.5, statistics.getMedian());
    }

    @Test
    public void getMedian_assessmentWithOddScores_returnsMiddleScore() {
        Map<Id, Score> oddScores = new HashMap<>(scores);
        oddScores.put(new Id("E0000011"), new Score("95"));
        assessment.setScores(oddScores);
        AssessmentStatistics statistics = new AssessmentStatistics(assessment);

        assertEquals(66, statistics.getMedian());
    }

    @Test
    public void getMedian_assessmentWithoutScores_returnsZeroScore() {
        assessment.setScores(new HashMap<>());
        AssessmentStatistics statistics = new AssessmentStatistics(assessment);

        assertEquals(Score.MIN_SCORE, statistics.getMedian());
    }

    @Test
    public void getMean_assessmentWithScores_returnsMean() {
        assessment.setScores(scores);
        AssessmentStatistics statistics = new AssessmentStatistics(assessment);

        assertEquals(56.9, statistics.getMean());
    }

    @Test
    public void getMean_assessmentWithoutScores_returnsZeroScore() {
        assessment.setScores(new HashMap<>());
        AssessmentStatistics statistics = new AssessmentStatistics(assessment);

        assertEquals(Score.MIN_SCORE, statistics.getMean());
    }

    @Test
    public void getXPercentile_assessmentWithScores_returnsXPercentile() {
        assessment.setScores(scores);
        AssessmentStatistics statistics = new AssessmentStatistics(assessment);

        assertEquals(46, statistics.getXPercentile(25));
        assertEquals(78, statistics.getXPercentile(75));
    }

    @Test
    public void getXPercentile_assessmentWithoutScores_returnsZeroScore() {
        assessment.setScores(new HashMap<>());
        AssessmentStatistics statistics = new AssessmentStatistics(assessment);

        assertEquals(Score.MIN_SCORE, statistics.getXPercentile(25));
    }
}
