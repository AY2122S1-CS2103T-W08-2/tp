package seedu.sourcecontrol.commons.util;

import java.util.ArrayList;
import java.util.Map;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * Creates JavaFX charts
 */
public class ChartUtil {
    private static final double DEFAULT_TICK_UNIT = 5.0;

    /**
     * Creates a JavaFX BarChart with the given title, axis labels and data points.
     */
    public static BarChart<String, Number> createBarChart(String title, String xLabel, String yLabel,
                                                          Map<String, Number> data) {
        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(xLabel);

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yLabel);
        // Disable auto-ranging so that we can configure our own tick units
        yAxis.setAutoRanging(false);
        yAxis.setTickUnit(DEFAULT_TICK_UNIT);

        final BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
        barChart.setTitle(title);
        barChart.setLegendVisible(false);

        double maxY = DEFAULT_TICK_UNIT;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Number> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            maxY = Math.max(maxY, entry.getValue().doubleValue());
        }

        double yUpperBound = roundUpToNearestMultiple(maxY, (int) DEFAULT_TICK_UNIT);

        // Add another tick unit as padding, else if there is a data point having a value which is a multiple of
        // the tick unit, the graph will look as if it is cut-off.
        yUpperBound += DEFAULT_TICK_UNIT;
        yAxis.setUpperBound(yUpperBound);

        barChart.getData().add(series);
        return barChart;
    }

    /**
     * Creates a JavaFX LineChart with the given title, axis labels and data points.
     */
    public static LineChart<String, Number> createLineChart(String title, String xLabel, String yLabel,
                                                            String dataLabel, Map<String, Number> data,
                                                            Map<String, Number> mean,
                                                            Map<String, Number> median) {

        // Defining axes
        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(xLabel);
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yLabel);

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);
        yAxis.setTickUnit(10);

        final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
        lineChart.setTitle(title);
        lineChart.setId("chart1");

        // Input score data points
        XYChart.Series<String, Number> seriesScore = new XYChart.Series<>();
        seriesScore.setName(dataLabel);
        for (Map.Entry<String, Number> entry : data.entrySet()) {
            seriesScore.getData().add(new XYChart.Data<>(wrap(entry.getKey()), entry.getValue()));
        }
        lineChart.getData().add(seriesScore);

        // Input mean data points
        XYChart.Series<String, Number> seriesMean = new XYChart.Series<>();
        seriesMean.setName("cohort mean");
        for (Map.Entry<String, Number> entry : mean.entrySet()) {
            seriesMean.getData().add(new XYChart.Data<>(wrap(entry.getKey()), entry.getValue()));
        }
        lineChart.getData().add(seriesMean);

        // Input median data points
        XYChart.Series<String, Number> seriesMedian = new XYChart.Series<>();
        seriesMedian.setName("cohort median");
        for (Map.Entry<String, Number> entry : median.entrySet()) {
            seriesMedian.getData().add(new XYChart.Data<>(wrap(entry.getKey()), entry.getValue()));
        }
        lineChart.getData().add(seriesMedian);

        return lineChart;
    }

    /**
     * Rounds up {@code double val} to multiples of {@code int multiple}.
     */
    public static double roundUpToNearestMultiple(double val, int multiple) {
        return Math.round(val / multiple) * multiple;
    }

    /**
     * Wraps the given string such that each line contains maximum of 12 characters, with a maximum of 3 lines.
     */
    public static String wrap(String string) {
        String[] words = string.split(" ");
        int count = 0;
        ArrayList<String> result = new ArrayList<>();
        int line = 0;

        while (count < words.length) {
            // check if exceed line limit
            if (line == 3) {
                break;
            }

            int currLineLength = 0;
            if (result.size() - 1 == line) {
                currLineLength = result.get(line).length();
            }
            int wordLength = words[count].length();

            // if nothing in current line, and next word can fit
            if (currLineLength == 0 && wordLength <= 12) {
                result.add(words[count]);
                count++;
                continue;
            }

            // word can fit into previous line
            if (wordLength + currLineLength + 1 <= 12) {
                result.set(line, result.get(line) + " " + words[count]);
                count++;
                continue;
            }

            // word cannot fit into previous line, but word is below size limit
            if (wordLength <= 12 && line < 2) {
                line++;
                result.add(words[count]);
                count++;
                continue;
            }

            // if nothing in curr line, but next word cannot fit
            if (currLineLength == 0) {
                result.add(words[count].substring(0, 12));
                words[count] = words[count].substring(12);
                line++;
                continue;
            }

            // if something in current line, fill up current line first
            int temp = 12 - 1 - currLineLength;
            if (temp < 0) {
                line++;
                continue;
            }
            result.set(line, result.get(line) + " " + words[count].substring(0, temp));
            words[count] = words[count].substring(temp);
            line++;
        }

        int numOfLines = result.size() - 1;

        if (count < words.length) {
            result.set(2, result.get(2).substring(0, 10) + "...");
        }

        String wrappedString = result.get(0);
        if (numOfLines >= 1) {
            wrappedString += "\n" + result.get(1);
        }
        if (numOfLines >= 2) {
            wrappedString += "\n" + result.get(2);
        }

        return wrappedString;

    }
}
