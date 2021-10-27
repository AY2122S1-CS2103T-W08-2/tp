package seedu.address.ui;


import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;


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
        placeHolder.getChildren().add(chart);
    }

    public void setPane(Pane pane) {
        placeHolder.getChildren().add(pane);
    }

    public void clearCharts() {
        placeHolder.getChildren().clear();
    }
}
