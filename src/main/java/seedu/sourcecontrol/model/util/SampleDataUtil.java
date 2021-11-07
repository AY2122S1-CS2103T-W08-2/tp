package seedu.sourcecontrol.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import seedu.sourcecontrol.model.ReadOnlySourceControl;
import seedu.sourcecontrol.model.SourceControl;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.Score;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.model.student.tag.Tag;

/**
 * Contains utility methods for populating {@code SourceControl} with sample data.
 */
public class SampleDataUtil {
    private static final Random random = new Random();

    //// Configuration variables
    private static final String[] SAMPLE_STUDENT_NAMES = {
        "How Kai Xin", "Wu Wei Le", "Ruchi Rattan", "Eva Porter", "Lai Guo Qiang Jack", "Tang Jia Xin",
        "Damini Kashyap", "Zhuang Yi Xi", "Winnie Ong Jia Ling", "Irfan Syahid Bin Mohammad Aydin", "Chen Kok Meng",
        "Sameera Raval", "Freya Murray", "Chang Yong Quan", "Haleigh Green", "Qian Jia Wen", "Ho Yi Xin",
        "Nur Sharifah Binte Azmi Noh", "Betania Suartini", "Zhuo Jia Wen", "Sim Ming Hao Jeremy", "Ng Sing Yu",
        "Rosey Shah", "Angus Johnson", "Lye Kai Feng", "Joel Jesus", "Nhung Phan", "Roberto Ferraro",
        "Qian Kai Wen", "Lew Si Hui", "Mohammad Syaril Bin Mohammad Irfan", "Loh Xin Yi", "Choo Yi Ling",
        "Juli Mandasari", "Jonathan Hong Yong Quan", "Teoh Yi Ling", "Leung Zheng Min", "Toh Hui Wen",
        "David Balasubramanian", "Tin Zhi Xin", "Asher Edwards", "Baey Yi De", "Low Hui Qi", "Chang Hao Ming",
        "Jackson Adams", "Edward Davidson", "Sim Hui Qi", "Kanika Kothari", "Tang Xin En", "Tin Zhi En"
    };
    // Max group number and suffix is kept sufficiently small so that each group has a reasonable number of students
    private static final int MAX_GROUP_NUMBER = 3;
    private static final char NUMBER_OF_SUFFIXES = 3;
    private static final String[] SAMPLE_TAG_NAMES = {"beginner", "intermediate", "advanced"};
    private static final String[] SAMPLE_ASSESSMENT_NAMES = {
        "Reading Assessment 1", "Reading Assessment 2", "Midterm Examination", "Practical Assessment",
        "Final Examination"
    };

    //// Shared instances
    private static final List<Group> SAMPLE_TUTORIAL_GROUPS = getRandomGroups("T");
    private static final List<Group> SAMPLE_RECITATION_GROUPS = getRandomGroups("R");
    private static final List<Assessment> SAMPLE_ASSESSMENTS = Arrays.stream(SAMPLE_ASSESSMENT_NAMES)
            .map(Assessment::new).collect(Collectors.toList());

    /**
     * Stores the IDs that have been randomly generated and created, to avoid generating duplicate IDs.
     */
    private static final Set<String> TAKEN_IDS = new HashSet<>();

    public static List<Student> getSampleStudents() {
        List<Student> sampleStudents = new ArrayList<>();

        for (String studentName : SAMPLE_STUDENT_NAMES) {
            Id studentId = getRandomStudentId();
            List<Group> groups = Arrays.asList(getRandomTutorialGroup(), getRandomRecitationGroup());
            for (Group group : groups) {
                group.addStudent(studentId);
            }
            Student student = new Student(new Name(studentName), studentId, groups,
                    getRandomAssessmentScores(), getTagSet(getRandomTag()));
            sampleStudents.add(student);
        }

        return sampleStudents;
    }

    public static ReadOnlySourceControl getSampleSourceControl() {
        SourceControl sampleAb = new SourceControl();
        for (Student sampleStudent : getSampleStudents()) {
            sampleAb.addStudent(sampleStudent);
        }
        return sampleAb;
    }

    /**
     * Generates a random {@code Id}. A random six digit number is generated and a '0' is prepended to it.
     * Ensures that only unique IDs will be generated each time.
     */
    public static Id getRandomStudentId() {
        int randomSixDigitNumber = random.nextInt((int) Math.pow(10, 6));
        String studentId = "E" + String.format("%07d", randomSixDigitNumber);

        if (TAKEN_IDS.contains(studentId)) {
            // If the generated student ID has already been taken, generate a new student ID.
            return getRandomStudentId();
        }

        TAKEN_IDS.add(studentId);
        return new Id(studentId);
    }

    public static List<Group> getRandomGroups(String prefix) {
        assert MAX_GROUP_NUMBER >= 1;
        List<Integer> groupNumbers = IntStream.rangeClosed(1, MAX_GROUP_NUMBER).boxed().collect(Collectors.toList());

        assert NUMBER_OF_SUFFIXES >= 1;
        List<Character> suffixes = IntStream.range(0, NUMBER_OF_SUFFIXES).boxed()
                .map(num -> (char) ('A' + num)).collect(Collectors.toList());

        List<Group> groups = new ArrayList<>();
        for (Integer groupNumber : groupNumbers) {
            for (Character suffix : suffixes) {
                String groupName = prefix + String.format("%02d", groupNumber) + suffix;
                groups.add(new Group(groupName));
            }
        }

        return groups;
    }

    /**
     * Returns a random group from the list of generated sample tutorial groups.
     */
    public static Group getRandomTutorialGroup() {
        return SAMPLE_TUTORIAL_GROUPS.get(random.nextInt(SAMPLE_TUTORIAL_GROUPS.size()));
    }

    /**
     * Returns a random group from the list of generated sample recitation groups.
     */
    public static Group getRandomRecitationGroup() {
        return SAMPLE_RECITATION_GROUPS.get(random.nextInt(SAMPLE_RECITATION_GROUPS.size()));
    }

    /**
     * Generates a score map for the sample assessments.
     */
    public static Map<Assessment, Score> getRandomAssessmentScores() {
        Map<Assessment, Score> scores = new LinkedHashMap<>();

        for (Assessment assessment : SAMPLE_ASSESSMENTS) {
            String s = String.format("%.2f", getRandomScore());
            scores.put(assessment, new Score(s));
        }

        return scores;
    }

    /**
     * Generates a random score, following a normal distribution with mean = 60 and standard deviation = 15.
     */
    public static double getRandomScore() {
        double randomScore = 60 + random.nextGaussian() * 15;
        randomScore = Math.min(randomScore, Score.MAX_SCORE);
        randomScore = Math.max(randomScore, Score.MIN_SCORE);
        return randomScore;
    }

    /**
     * Gets a random tag from the sample tags.
     */
    public static String getRandomTag() {
        return SAMPLE_TAG_NAMES[random.nextInt(SAMPLE_TAG_NAMES.length)];
    }

    /**
     * Returns a group list containing the list of strings given.
     */
    public static List<Group> getGroupList(String... strings) {
        return Arrays.stream(strings)
                .map(Group::new)
                .collect(Collectors.toList());
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
