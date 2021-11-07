package seedu.sourcecontrol.model.student.assessment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;

public class GroupStatisticsTest {

    private final Id aliceId = new Id("E0000001");
    private final Id benId = new Id("E0000002");

    private final Student alice = new Student(new Name("Alice"), aliceId);
    private final Student ben = new Student(new Name("Ben"), benId);

    private final Group group = new Group("T01A", Arrays.asList(aliceId, benId));

    private final Assessment p01 = new Assessment("P01");
    private final Assessment p02 = new Assessment("P02");
    private final Assessment p03 = new Assessment("P03");

    private final List<Assessment> assessmentList = Arrays.asList(p01, p02);

    private final Map<Assessment, Score> aliceScore = Map.of(
            p01, new Score("78"),
            p02, new Score("66")
    );

    private final Map<Assessment, Score> benScore = Map.of(
            p01, new Score("44"),
            p02, new Score("90")
    );

    private final Map<Id, Score> p01Score = Map.of(
            aliceId, new Score("78"),
            benId, new Score("44")
    );

    private final Map<Id, Score> p02Score = Map.of(
            aliceId, new Score("66"),
            benId, new Score("90")
    );

    private final Map<Id, Score> p01Score2 = Map.of(
            aliceId, new Score("78"),
            benId, new Score("44"),
            new Id("E0000003"), new Score("79")
    );

    private final Map<Id, Score> p02Score2 = Map.of(
            aliceId, new Score("66"),
            benId, new Score("90"),
            new Id("E0000003"), new Score("87")
    );


    @Test
    public void isGraded() {
        alice.addScores(aliceScore);
        p01.setScores(p01Score);
        p02.setScores(p02Score);
        GroupStatistics statistics = new GroupStatistics(group, assessmentList);

        assertTrue(statistics.isGraded(p01));
        assertTrue(statistics.isGraded(p02));
        assertFalse(statistics.isGraded(p03));
    }

    @Test
    public void getMedian_returnCorrectMedian() {
        alice.addScores(aliceScore);
        ben.addScores(benScore);
        p01.setScores(p01Score);
        p02.setScores(p02Score);
        GroupStatistics statistics = new GroupStatistics(group, assessmentList);

        assertEquals(statistics.getMedian(p01), 61);
        assertEquals(statistics.getMedian(p02), 78);
    }


    @Test
    public void getDataSet_returnsCorrectDataSet() {
        alice.addScores(aliceScore);
        ben.addScores(benScore);
        p01.setScores(p01Score2);
        p02.setScores(p02Score2);
        GroupStatistics statistics = new GroupStatistics(group, assessmentList);

        Map<String, Number> groupMedian = Map.of(
                "P01", 61.0,
                "P02", 78.0
        );
        assertEquals(groupMedian, statistics.getDataSet()[0]);

        Map<String, Number> expectedMean = Map.of(
                "P01", 67.0,
                "P02", 81.0
        );
        assertEquals(expectedMean, statistics.getDataSet()[1]);

        Map<String, Number> expectedMedian = Map.of(
                "P01", 78.0,
                "P02", 87.0
        );
        assertEquals(expectedMedian, statistics.getDataSet()[2]);
    }
}
