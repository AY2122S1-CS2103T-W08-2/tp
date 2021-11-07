package seedu.sourcecontrol.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.sourcecontrol.commons.core.GuiSettings;
import seedu.sourcecontrol.commons.core.LogsCenter;
import seedu.sourcecontrol.logic.commands.Command;
import seedu.sourcecontrol.logic.commands.CommandResult;
import seedu.sourcecontrol.logic.commands.exceptions.CommandException;
import seedu.sourcecontrol.logic.parser.Alias;
import seedu.sourcecontrol.logic.parser.AliasCommandParser;
import seedu.sourcecontrol.logic.parser.SourceControlParser;
import seedu.sourcecontrol.logic.parser.exceptions.ParseException;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ReadOnlySourceControl;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final SourceControlParser sourceControlParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        sourceControlParser = new SourceControlParser();
        loadAliases();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = sourceControlParser.parseCommand(commandText);
        commandResult = command.execute(model);

        try {
            storage.saveSourceControl(model.getSourceControl());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlySourceControl getSourceControl() {
        return model.getSourceControl();
    }

    @Override
    public ObservableList<Student> getFilteredStudentList() {
        return model.getFilteredStudentList();
    }

    @Override
    public Path getSourceControlFilePath() {
        return model.getSourceControlFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    /**
     * Loads the aliases from UserPrefs into the parser.
     */
    public void loadAliases() {
        Map<String, String> aliases = model.getAliases();

        // Verify that all imported aliases are valid. If any are not valid, remove the alias from the UserPrefs
        for (String aliasWord : aliases.keySet()) {
            try {
                AliasCommandParser.checkAliasWord(aliasWord, sourceControlParser);
                AliasCommandParser.checkCommandWord(aliases.get(aliasWord), sourceControlParser);
            } catch (ParseException e) {
                logger.info("Invalid alias removed: " + aliasWord);
                aliases.remove(aliasWord);
            }
        }

        model.setAliases(aliases);

        for (Map.Entry<String, String> alias: aliases.entrySet()) {
            sourceControlParser.addAlias(new Alias(alias.getKey(), alias.getValue()));
        }
    }
}
