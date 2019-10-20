package extremesaving.service.chart;

import extremesaving.service.ChartDataService;
import extremesaving.service.pdf.PdfPageTipOfTheDayService;
import extremesaving.util.PdfUtils;
import extremesaving.util.PropertiesValueHolder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static extremesaving.util.PropertyValueENum.GOAL_LINE_CHART_IMAGE_FILE;

public class GoalLineChartService implements ChartService {

    private ChartDataService chartDataService;

    @Override
    public void generateChartPng() {
        JFreeChart chart = ChartFactory.createTimeSeriesChart("", "", "", createDataset(), false, false, false);
        PdfUtils.writeChartPng(chart, PropertiesValueHolder.getInstance().getPropValue(GOAL_LINE_CHART_IMAGE_FILE), (int) PdfPageTipOfTheDayService.GOAL_LINE_CHART_WIDTH * 2, (int) PdfPageTipOfTheDayService.GOAL_LINE_CHART_HEIGHT * 2);
    }

    private TimeSeriesCollection createDataset() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        TimeSeries series1 = new TimeSeries("Balance history");
        TimeSeries series2 = new TimeSeries("Estimated result");

        Map<Date, BigDecimal> goalLineResults = chartDataService.getGoalLineResults();

        final Date today = new Date();
        for (Map.Entry<Date, BigDecimal> result : goalLineResults.entrySet()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(result.getKey());
            if (cal.getTime().before(today)) {
                series1.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
            } else {
                series2.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
            }
        }

        dataset.addSeries(series1);
        dataset.addSeries(series2);

        return dataset;
    }

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}