package seedu.sourcecontrol.logic.commands;

import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.sourcecontrol.testutil.TypicalStudents.getTypicalSourceControl;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ModelManager;
import seedu.sourcecontrol.model.SourceControl;
import seedu.sourcecontrol.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptySourceControl_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptySourceControl_success() {
        Model model = new ModelManager(getTypicalSourceControl(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalSourceControl(), new UserPrefs());
        expectedModel.setSourceControl(new SourceControl());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
