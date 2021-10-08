package seedu.address.testutil;

import seedu.address.model.student.ID;
import seedu.address.model.student.Name;
import seedu.address.model.student.Student;

import java.util.HashSet;

public class StudentStub extends Student {
    public StudentStub() {
        super(new Name("a"), new ID("E0192381"), new HashSet<>());
    }
}
