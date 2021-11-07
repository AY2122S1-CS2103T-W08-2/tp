package seedu.sourcecontrol.model.student.assessment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;

public class StudentStatisticsTest {
    private final Student student = new Student(new Name("Alice Wong"), new Id("E0000001"));

    private final Map<Assessment, Score> scores = Map.of(
            new Assessment("P01"), new Score("5.5"),
            new Assessment("P02"), new Score("13.3"),
            new Assessment("P03"), new Score("46.7"),
            new Assessment("P04"), new Score("48.34"),
            new Assessment("Reading Assessment 1"), new Score("55.1"),
            new Assessment("Reading Assessment 2"), new Score("66.34"),
            new Assessment("Practical Assessment 1"), new Score("70.0"),
            new Assessment("Practical Assessment 2"), new Score("78.4"),
            new Assessment("Midterm Examination"), new Score("88.9"),
            new Assessment("Final Examination"), new Score("100")
    );

    private final Map<Id, Score> p01Score = Map.of(
            new Id("E0000001"), new Score("5.5"),
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

    private final Assessment p01 = new Assessment("P01");

    private final Map<Id, Score> p02Score = Map.of(
            new Id("E0000001"), new Score("13.3"),
            new Id("E0000002"), new Score("44.7"),
            new Id("E0000003"), new Score("23"),
            new Id("E0000004"), new Score("67"),
            new Id("E0000005"), new Score("35"),
            new Id("E0000006"), new Score("98"),
            new Id("E0000007"), new Score("32"),
            new Id("E0000008"), new Score("74"),
            new Id("E0000009"), new Score("99"),
            new Id("E0000010"), new Score("100")
    );

    private final Assessment p02 = new Assessment("P02");

    private final Map<Assessment, Score> scores2 = Map.of(
            p01, new Score("5.5"),
            p02, new Score("13.3")
    );


    @Test
    public void getScoreDistribution_returnCorrectScoreDistribution() {
        student.addScores(scores);
        StudentStatistics statistics = new StudentStatistics(student);

        Map<String, Number> expectedScoreDistribution = Map.of(
                "P01", 5.5,
                "P02", 13.3,
                "P03", 46.7,
                "P04", 48.34,
                "Reading Assessment 1", 55.1,
                "Reading Assessment 2", 66.34,
                "Practical Assessment 1", 70.0,
                "Practical Assessment 2", 78.4,
                "Midterm Examination", 88.9,
                "Final Examination", 100.0
        );

        assertEquals(expectedScoreDistribution, statistics.getScoreDistribution());
    }

    @Test
    public void getDataSet_returnsCorrectDataSet() {
        student.addScores(scores2);
        StudentStatistics statistics = new StudentStatistics(student);

        p01.setScores(p01Score);
        p02.setScores(p02Score);

        Map<String, Number> expectedMean = Map.of(
                "P01", 56.95,
                "P02", 58.6
        );
        assertEquals(expectedMean, statistics.getDataSet()[0]);

        Map<String, Number> expectedMedian = Map.of(
                "P01", 60.5,
                "P02", 55.85
        );
        assertEquals(expectedMedian, statistics.getDataSet()[1]);
    }
}
