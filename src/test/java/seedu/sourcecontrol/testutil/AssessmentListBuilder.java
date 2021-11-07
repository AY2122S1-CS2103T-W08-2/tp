package seedu.sourcecontrol.testutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.AssessmentList;
import seedu.sourcecontrol.model.student.assessment.Score;

public class AssessmentListBuilder {

    private List<Assessment> assessments;

    /**
     * Creates a {@code AssessmentListBuilder} with the default details.
     */
    public AssessmentListBuilder() {
        assessments = new ArrayList<>();
    }

    /**
     * Sets the {@code students} of the {@code assessmentList} that we are building.
     */
    public AssessmentListBuilder withStudents(List<Student> students) {
        List<Assessment> result = new ArrayList<>();
        for (Student student : students) {
            Map<Assessment, Score> studentScores = student.getScores();
            for (Assessment assessment : studentScores.keySet()) {
                if (!result.contains(assessment)) {
                    result.add(assessment);
                }
                Assessment resultAssessment = result.get(result.indexOf(assessment));
                resultAssessment.setScore(student.getId(), studentScores.get(assessment));
            }
        }
        this.assessments = result;
        return this;
    }

    /**
     * Returns an {@code AssessmentList}.
     */
    public AssessmentList build() {
        AssessmentList assessmentList = new AssessmentList();
        assessmentList.setAssessments(assessments);
        return assessmentList;
    }
}
