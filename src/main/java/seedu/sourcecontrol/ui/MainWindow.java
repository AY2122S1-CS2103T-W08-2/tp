package seedu.sourcecontrol.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Chart;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.sourcecontrol.commons.core.GuiSettings;
import seedu.sourcecontrol.commons.core.LogsCenter;
import seedu.sourcecontrol.commons.util.FileUtil;
import seedu.sourcecontrol.logic.Logic;
import seedu.sourcecontrol.logic.commands.CommandResult;
import seedu.sourcecontrol.logic.commands.ShowCommand;
import seedu.sourcecontrol.logic.commands.exceptions.CommandException;
import seedu.sourcecontrol.logic.parser.exceptions.ParseException;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    public static final String GRAPH_SAVE_FAIL = "Graph failed to save";
    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private double divideRatio;

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private StudentListPanel studentListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private InfoDisplay infoDisplay;
    private GraphDisplay graphDisplay;

    @FXML
    private SplitPane splitPane;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane studentListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane infoDisplayPlaceholder;

    @FXML
    private StackPane graphDisplayPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        studentListPanel = new StudentListPanel(logic.getFilteredStudentList());
        studentListPanelPlaceholder.getChildren().add(studentListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getSourceControlFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        infoDisplay = new InfoDisplay();
        infoDisplayPlaceholder.getChildren().add(infoDisplay.getRoot());

        graphDisplay = new GraphDisplay();
        graphDisplayPlaceholder.getChildren().add(graphDisplay.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
        this.divideRatio = guiSettings.getDividerPosition();
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        double[] dividerPositions = splitPane.getDividerPositions();
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY(), dividerPositions[0]);
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    public StudentListPanel getStudentListPanel() {
        return studentListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.sourcecontrol.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
            // Clear any old info or charts
            infoDisplay.clearInfo();
            graphDisplay.clearCharts();

            if (commandResult.hasInfo()) {
                ShowCommand.Info info = commandResult.getInfo();
                assert info != null;
                infoDisplay.setInfo(info);
            }

            if (commandResult.hasChart()) {
                Chart chart = commandResult.getChart();
                assert chart != null;
                graphDisplay.setChart(chart);
                if (commandResult.hasSavePath()) {
                    saveGraph(commandResult.getSavePath());
                }
            }

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    public void loadDividerRatio() {
        splitPane.setDividerPositions(divideRatio);
    }

    private void saveGraph(Path savePath) throws CommandException {
        assert savePath != null;
        WritableImage img = graphDisplayPlaceholder.snapshot(new SnapshotParameters(), null);
        int width = (int) img.getWidth();
        int height = (int) img.getHeight();

        PixelReader pixelReader = img.getPixelReader();

        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bimg.setRGB(i, j, pixelReader.getArgb(i, j));
            }
        }

        File file = savePath.toFile();

        try {
            FileUtil.createIfMissing(savePath);
            ImageIO.write(bimg, "png", file);
        } catch (IOException e) {
            throw new CommandException(GRAPH_SAVE_FAIL);
        }

        resultDisplay.setFeedbackToUser("Graph saved successfully at " + savePath);
    }
}
