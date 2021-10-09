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
    public static Student[] getSamplePersons() {
        return new Student[] {
            new Student(new Name("Alex Yeoh"), new ID("E0534794"),
                getGroupList("T02A", "R03A"),
                Map.of(new Assessment("P01"), new Score("89"),
                        new Assessment("M01"), new Score("100")),
                getTagSet("friends")),
            new Student(new Name("Bernice Yu"), new ID("E0481923"),
                getGroupList("T07A", "R10B"),
                Map.of(new Assessment("M01"), new Score("100"),
                        new Assessment("Q01"), new Score("100")),
                getTagSet("colleagues", "friends")),
            new Student(new Name("Charlotte Oliveiro"), new ID("E0348902"),
                getGroupList("T10H", "R03A"),
                Map.of(new Assessment("P03"), new Score("66.6")),
                getTagSet("neighbours")),
            new Student(new Name("David Li"), new ID("E0345242"),
                getGroupList("T09B", "R04B"),
                Map.of(new Assessment("M02"), new Score("100.0")),
                getTagSet("family")),
            new Student(new Name("Irfan Ibrahim"), new ID("E0593933"),
                getGroupList("T02J", "R09A"),
                Map.of(new Assessment("M04"), new Score("74")),
                getTagSet("classmates")),
            new Student(new Name("Roy Balakrishnan"), new ID("E0539221"),
                getGroupList("T08C", "R01C"),
                Map.of(new Assessment("P13"), new Score("83")),
                getTagSet("colleagues"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Student sampleStudent : getSamplePersons()) {
            sampleAb.addStudent(sampleStudent);
        }
        return sampleAb;
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
