package extremesaving.charts.builder;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class GoalLineChart {

    private Map<Date, BigDecimal> historyResults;
    private Map<Date, BigDecimal> survivalResults;
    private Map<Date, BigDecimal> futureResults;

    public GoalLineChart withHistoryResults(Map<Date, BigDecimal> historyResults) {
        this.historyResults = historyResults;
        return this;
    }

    public GoalLineChart withSurvivalResults(Map<Date, BigDecimal> survivalResults) {
        this.survivalResults = survivalResults;
        return this;
    }

    public GoalLineChart withFutureResults(Map<Date, BigDecimal> futureResults) {
        this.futureResults = futureResults;
        return this;
    }

    public JFreeChart build() {
        return ChartFactory.createTimeSeriesChart("", "", "", createDataset(), false, false, false);
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
            series.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
        }
        return series;
    }

    protected TimeSeries getFutureSeries() {
        TimeSeries series = new TimeSeries("Estimated result");
        for (Map.Entry<Date, BigDecimal> result : futureResults.entrySet()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(result.getKey());
            series.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
        }
        return series;
    }
}