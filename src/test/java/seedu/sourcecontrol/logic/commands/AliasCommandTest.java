package seedu.sourcecontrol.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.logic.parser.Alias;
import seedu.sourcecontrol.logic.parser.SourceControlParser;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ModelManager;

public class AliasCommandTest {
    private final Model model = new ModelManager();
    private final SourceControlParser parser = new SourceControlParser();

    @Test
    public void execute_success() {
        AliasCommand command = new AliasCommand(new Alias("aliasWord", "commandWord"), parser);
        Model expectedModel = new ModelManager();
        expectedModel.addAlias(new Alias("aliasWord", "commandWord"));
        assertCommandSuccess(command, model,
                String.format(AliasCommand.MESSAGE_ADD_SUCCESS, "aliasWord"), expectedModel);

        SourceControlParser expectedParser = new SourceControlParser();
        expectedParser.addAlias(new Alias("aliasWord", "commandWord"));
        assertEquals(parser, expectedParser);

        command = new AliasCommand(new Alias("aliasWord", "aliasWord"), parser);
        expectedModel = new ModelManager();
        assertCommandSuccess(command, model,
                String.format(AliasCommand.MESSAGE_REMOVE_SUCCESS, "aliasWord"), expectedModel);

        expectedParser = new SourceControlParser();
        assertEquals(parser, expectedParser);
    }
}
