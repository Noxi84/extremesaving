package extremesaving.charts.service;

import extremesaving.pdf.service.PdfPageTipOfTheDayService;
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
        chartDataService.writeChartPng(chart, PropertiesValueHolder.getString(YEAR_LINE_CHART_IMAGE_FILE), (int) PdfPageTipOfTheDayService.YEAR_LINE_CHART_WIDTH * 2, (int) PdfPageTipOfTheDayService.YEAR_LINE_CHART_HEIGHT * 2);
    }

    protected TimeSeriesCollection createDataset() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        TimeSeries series1 = new TimeSeries("Estimated result");
        TimeSeries series2 = new TimeSeries("Balance history");
        TimeSeries series3 = new TimeSeries("Without incomes");

        Map<Date, BigDecimal> goalLineResults = chartDataService.getGoalLineResults();

        Date today = new Date();
        for (Map.Entry<Date, BigDecimal> result : goalLineResults.entrySet()) {
            if (DateUtils.equalYears(result.getKey(), today)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(result.getKey());
                if (cal.getTime().before(today)) {
                    series2.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
                } else {
                    series1.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
                    series3.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
                }
            }
        }

        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);

        return dataset;
    }

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}