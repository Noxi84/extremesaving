package extremesaving.service.chart;

import extremesaving.service.ChartDataService;
import extremesaving.service.pdf.PdfPageCategoryGridService;
import extremesaving.util.DateUtils;
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

import static extremesaving.util.PropertyValueENum.MONTH_LINE_CHART_IMAGE_FILE;

public class MonthLineChartService implements ChartService {

    private ChartDataService chartDataService;

    @Override
    public void generateChartPng() {
        JFreeChart chart = ChartFactory.createTimeSeriesChart("", "", "", createDataset(), false, false, false);
        PdfUtils.writeChartPng(chart, PropertiesValueHolder.getInstance().getPropValue(MONTH_LINE_CHART_IMAGE_FILE), (int) PdfPageCategoryGridService.CHART_WIDTH * 2, (int) PdfPageCategoryGridService.CHART_HEIGHT * 2);
    }

    private TimeSeriesCollection createDataset() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        TimeSeries series1 = new TimeSeries("");

        Map<Date, BigDecimal> goalLineResults = chartDataService.getGoalLineResults();

        Date today = new Date();
        for (Map.Entry<Date, BigDecimal> result : goalLineResults.entrySet()) {
            if (DateUtils.equalYears(result.getKey(), today)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(result.getKey());
                series1.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
            }
        }

        dataset.addSeries(series1);

        return dataset;
    }

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}