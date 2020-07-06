package extremesaving.charts.builder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.util.ShapeUtilities;

/**
 * Builder for the goal-line chart.
 */
public class OverallLineChartBuilder {

    private Map<Date, BigDecimal> historyResults;
    private Map<Date, BigDecimal> survivalResults;
    private Map<Date, BigDecimal> futureResults;

    /**
     * Add the history results line on the chart. This is the blue line.
     *
     * @param historyResults Map<Date, BigDecimal> end result for each day.
     * @return OverallChartBuilder
     */
    public OverallLineChartBuilder withHistoryResults(Map<Date, BigDecimal> historyResults) {
        this.historyResults = historyResults;
        return this;
    }

    /**
     * Add the survial results on the chart. This is the red line.
     *
     * @param survivalResults Map<Date, BigDecimal> end result for each day.
     * @return OverallLineChartBuilder
     */
    public OverallLineChartBuilder withSurvivalResults(Map<Date, BigDecimal> survivalResults) {
        this.survivalResults = survivalResults;
        return this;
    }

    /**
     * Add the future results on the chart. This is the green line.
     *
     * @param futureResults Map<Date, BigDecimal> end result for each day.
     * @return OverallLineChartBuilder
     */
    public OverallLineChartBuilder withFutureResults(Map<Date, BigDecimal> futureResults) {
        this.futureResults = futureResults;
        return this;
    }

    /**
     * Build the JFreeChart object.
     *
     * @return JFreeChart
     */
    public JFreeChart build() {
        JFreeChart jFreeChart = ChartFactory.createTimeSeriesChart("", "", "", createDataset(), false, false, false);

        // Using the CardinalSplineRenderer.
        final XYCardinalSplineRenderer csRenderer = new XYCardinalSplineRenderer();
        csRenderer.setDrawOutlines(true);

        // Series 1.
        csRenderer.setSeriesShapesVisible(2, false);
        csRenderer.setSeriesPaint(2, Color.red);

        // Series 2.
        csRenderer.setSeriesShapesVisible(0, false);
        csRenderer.setSeriesPaint(0, Color.green);
        // Series 3.

        csRenderer.setSeriesShapesVisible(1, false);
        csRenderer.setSeriesPaint(1, Color.blue);

        XYPlot plot = (XYPlot) jFreeChart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainZeroBaselineVisible(true);
        plot.setRangeZeroBaselineVisible(true);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlineStroke(new BasicStroke(0.5F, 0, 1));
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlineStroke(new BasicStroke(0.5F, 0, 1));
        plot.setRenderer(0, csRenderer);

        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setAutoRangeIncludesZero(false);
        range.setAxisLineVisible(false);
        range.setLabelFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        return jFreeChart;
    }

    protected TimeSeriesCollection createDataset() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(getFutureSeries());
        dataset.addSeries(getHistorySeries());
        dataset.addSeries(getSurvivalSeries());
        return dataset;
    }

    protected TimeSeries getHistorySeries() {
        TimeSeries series = new TimeSeries("Balance history");
        for (Map.Entry<Date, BigDecimal> result : historyResults.entrySet()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(result.getKey());
            series.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
        }
        return series;
    }

    protected TimeSeries getSurvivalSeries() {
        TimeSeries series = new TimeSeries("Without incomes");
        for (Map.Entry<Date, BigDecimal> result : survivalResults.entrySet()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(result.getKey());
            if (cal.get(Calendar.YEAR) <= 9999) {
                series.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
            }
        }
        return series;
    }

    protected TimeSeries getFutureSeries() {
        TimeSeries series = new TimeSeries("Estimated result");
        for (Map.Entry<Date, BigDecimal> result : futureResults.entrySet()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(result.getKey());
            if (cal.get(Calendar.YEAR) <= 9999) {
                series.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
            }
        }
        return series;
    }
}