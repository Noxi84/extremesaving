package extremesaving.charts.service;

import extremesaving.charts.GoalLineResultEnum;
import extremesaving.pdf.page.tipoftheday.section.GoalLineChartPdfSectionCreator;
import extremesaving.property.PropertiesValueHolder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static extremesaving.property.PropertyValueEnum.GOAL_LINE_CHART_IMAGE_FILE;

public class GoalLineChartService implements ChartService {

    private ChartDataService chartDataService;

    @Override
    public void generateChartPng() {
        JFreeChart chart = ChartFactory.createTimeSeriesChart("", "", "", createDataset(), false, false, false);
        chartDataService.writeChartPng(chart, PropertiesValueHolder.getString(GOAL_LINE_CHART_IMAGE_FILE), (int) GoalLineChartPdfSectionCreator.GOAL_LINE_CHART_WIDTH * 2, (int) GoalLineChartPdfSectionCreator.GOAL_LINE_CHART_HEIGHT * 2);
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
        Map<Date, BigDecimal> historyResults = chartDataService.getGoalLineResults(GoalLineResultEnum.HISTORY);
        for (Map.Entry<Date, BigDecimal> result : historyResults.entrySet()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(result.getKey());
            series.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
        }
        return series;
    }

    protected TimeSeries getSurvivalSeries() {
        TimeSeries series = new TimeSeries("Without incomes");
        Map<Date, BigDecimal> survivalResults = chartDataService.getGoalLineResults(GoalLineResultEnum.SURVIVAL_ESTIMATION);
        for (Map.Entry<Date, BigDecimal> result : survivalResults.entrySet()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(result.getKey());
            series.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
        }
        return series;
    }

    protected TimeSeries getEstimationSeries() {
        TimeSeries series = new TimeSeries("Estimated result");
        Map<Date, BigDecimal> futureResults = chartDataService.getGoalLineResults(GoalLineResultEnum.FUTURE_ESTIMATION);
        for (Map.Entry<Date, BigDecimal> result : futureResults.entrySet()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(result.getKey());
            series.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
        }
        return series;
    }

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}