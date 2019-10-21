package extremesaving.charts.service;

import extremesaving.pdf.page.tipoftheday.component.YearLineChartPdfSectionCreator;
import extremesaving.property.PropertiesValueHolder;
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

import static extremesaving.property.PropertyValueEnum.YEAR_LINE_CHART_IMAGE_FILE;

public class YearLineChartService implements ChartService {

    private ChartDataService chartDataService;

    @Override
    public void generateChartPng() {
        JFreeChart chart = ChartFactory.createTimeSeriesChart("", "", "", createDataset(), false, false, false);
        chartDataService.writeChartPng(chart, PropertiesValueHolder.getString(YEAR_LINE_CHART_IMAGE_FILE), (int) YearLineChartPdfSectionCreator.YEAR_LINE_CHART_WIDTH * 2, (int) YearLineChartPdfSectionCreator.YEAR_LINE_CHART_HEIGHT * 2);
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
        Map<Date, BigDecimal> historyResults = chartDataService.getGoalLineHistoryResults();
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
        Map<Date, BigDecimal> survivalResults = chartDataService.getGoalLineSurvivalEstimationResults();
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
        Map<Date, BigDecimal> futureResults = chartDataService.getGoalLineFutureEstimationResults();

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

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}