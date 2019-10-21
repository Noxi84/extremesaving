package extremesaving.charts.builder;

import extremesaving.util.DateUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class YearLineChart {

    private Map<Date, BigDecimal> historyResults;
    private Map<Date, BigDecimal> survivalResults;
    private Map<Date, BigDecimal> futureResults;

    public YearLineChart withHistoryResults(Map<Date, BigDecimal> historyResults) {
        this.historyResults = historyResults;
        return this;
    }

    public YearLineChart withSurvivalResults(Map<Date, BigDecimal> survivalResults) {
        this.survivalResults = survivalResults;
        return this;
    }

    public YearLineChart withFutureResults(Map<Date, BigDecimal> futureResults) {
        this.futureResults = futureResults;
        return this;
    }

    public JFreeChart build() {
        return ChartFactory.createTimeSeriesChart("", "", "", createDataset(), false, false, false);
    }

    protected TimeSeriesCollection createDataset() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(getEstimationSeries());
        dataset.addSeries(getBalanceHistorySeries());
        dataset.addSeries(getSurvivalSeries());
        return dataset;
    }

    protected TimeSeries getBalanceHistorySeries() {
        TimeSeries series = new TimeSeries("Balance history");
        Date today = new Date();
        for (Map.Entry<Date, BigDecimal> result : historyResults.entrySet()) {
            if (DateUtils.equalYears(result.getKey(), today)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(result.getKey());
                series.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
            }
        }
        return series;
    }

    protected TimeSeries getSurvivalSeries() {
        TimeSeries series = new TimeSeries("Without incomes");
        Date today = new Date();
        for (Map.Entry<Date, BigDecimal> result : survivalResults.entrySet()) {
            if (DateUtils.equalYears(result.getKey(), today)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(result.getKey());
                series.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
            }
        }
        return series;
    }

    protected TimeSeries getEstimationSeries() {
        TimeSeries series = new TimeSeries("Estimated result");
        Date today = new Date();
        for (Map.Entry<Date, BigDecimal> result : futureResults.entrySet()) {
            if (DateUtils.equalYears(result.getKey(), today)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(result.getKey());
                series.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
            }
        }
        return series;
    }
}