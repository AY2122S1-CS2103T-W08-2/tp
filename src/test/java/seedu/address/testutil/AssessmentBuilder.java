package seedu.address.testutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import seedu.address.model.student.Assessment;
import seedu.address.model.student.Score;
import seedu.address.model.student.Student;

public class AssessmentBuilder {
    /**
     * Takes in a list of students with scores, and returns a list of assessments with those scores reflected
     */
    public static List<Assessment> createAssessmentListFromStudents(List<Student> students) {
        List<Assessment> result = new ArrayList<>();
        for (Student student : students) {
            Map<Assessment, Score> studentScores = student.getScores();
            for (Assessment assessment : studentScores.keySet()) {
                if (!result.contains(assessment)) {
                    result.add(assessment);
                }
                Assessment resultAssessment = result.get(result.indexOf(assessment));
                resultAssessment.setScores(student.getId(), studentScores.get(assessment));
            }
        }
        return result;
    }
}
