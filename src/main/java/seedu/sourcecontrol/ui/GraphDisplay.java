package seedu.sourcecontrol.ui;


import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class GraphDisplay extends UiPart<Region> {

    private static final String FXML = "GraphDisplay.fxml";

    @FXML
    private StackPane placeHolder;

    @FXML
    private TextArea resultDisplay;

    public GraphDisplay() {
        super(FXML);
    }

    public void setChart(Chart chart) {
        // to make the image save properly
        chart.setAnimated(false);
        placeHolder.getChildren().add(chart);
    }

    public void clearCharts() {
        placeHolder.getChildren().clear();
    }
}
