package seedu.address.model.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.student.Assessment;
import seedu.address.model.student.Group;
import seedu.address.model.student.ID;
import seedu.address.model.student.Name;
import seedu.address.model.student.Score;
import seedu.address.model.student.Student;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    // TODO: update these so that the assessments and scores are shared
    public static Assessment[] getSampleAssessments() {
        return new Assessment[] {
                new Assessment("P01"),
                new Assessment("M01"),
                new Assessment("Q01"),
                new Assessment("P03"),
                new Assessment("M02"),
                new Assessment("M04"),
                new Assessment("Reading Assessment 1"),
                new Assessment("Midterms")
        };
    }

    public static Group[] getSampleGroups() {
        return new Group[] {
                new Group("T02A"),
                new Group("R03A"),
                new Group("T07A"),
                new Group("R10B"),
                new Group("T10H"),
                new Group("Remedial")
        };
    }

    public static Student[] getSampleStudents() {
        return new Student[] {
            new Student(new Name("Alex Yeoh"), new ID("E0534794"), getTagSet("friends")),
            new Student(new Name("Bernice Yu"), new ID("E0481923"), getTagSet("colleagues", "friends")),
            new Student(new Name("Charlotte Oliveiro"), new ID("E0348902"), getTagSet("neighbours")),
            new Student(new Name("David Li"), new ID("E0345242"), getTagSet("family")),
            new Student(new Name("Irfan Ibrahim"), new ID("E0593933"), getTagSet("classmates")),
            new Student(new Name("Roy Balakrishnan"), new ID("E0539221"), getTagSet("colleagues"))
        };
    }

    public static void updateScores(Assessment[] assessments, Student[] students) {
        // arbitrarily give each user some scores
        String[] possibleScores = new String[] {"66.6", "100", "89", "74", "83", "42"};
        for (int i = 0; i < assessments.length; i++) {
            for (int j = 0; j < students.length; j++) {
                Score.updateScore(assessments[i], students[j], possibleScores[(i * j + j) / possibleScores.length]);
            }
        }
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        Student[] students = getSampleStudents();
        Assessment[] assessments = getSampleAssessments();
        updateScores(assessments, students);

        Group[] groups = getSampleGroups();

        // give each student a tutorial and a recitation, and give some remedial
        students[0].addGroup(groups[0]);
        students[0].addGroup(groups[1]);
        students[1].addGroup(groups[0]);
        students[1].addGroup(groups[3]);
        students[2].addGroup(groups[2]);
        students[2].addGroup(groups[3]);
        students[3].addGroup(groups[4]);
        students[3].addGroup(groups[1]);
        students[3].addGroup(groups[5]);
        students[4].addGroup(groups[2]);
        students[4].addGroup(groups[1]);
        students[5].addGroup(groups[0]);
        students[5].addGroup(groups[1]);
        students[5].addGroup(groups[5]);
        students[6].addGroup(groups[2]);
        students[6].addGroup(groups[3]);

        for (Student sampleStudent : students) {
            sampleAb.addStudent(sampleStudent);
        }

        for (Assessment sampleAssessment : assessments) {
            sampleAb.addAssessment(sampleAssessment);
        }

        for (Group sampleGroup : groups) {
            sampleAb.addGroup(sampleGroup);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
