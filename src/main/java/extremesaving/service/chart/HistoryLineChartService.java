package extremesaving.service.chart;

import extremesaving.service.ChartDataService;
import extremesaving.util.ChartUtils;
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

import static extremesaving.util.PropertyValueENum.HISTORY_LINE_CHART_IMAGE_FILE;

public class HistoryLineChartService implements ChartService {

    private ChartDataService chartDataService;

    @Override
    public void generateChartPng() {
        JFreeChart chart = ChartFactory.createTimeSeriesChart("", "", "", createDataset(), false, false, false);
        ChartUtils.writeChartPng(chart, PropertiesValueHolder.getInstance().getPropValue(HISTORY_LINE_CHART_IMAGE_FILE), 760, 600);
    }

    private TimeSeriesCollection createDataset() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        TimeSeries series1 = new TimeSeries("");

        Map<Date, BigDecimal> historyLineResults = chartDataService.getHistoryLineResults();

        for (Map.Entry<Date, BigDecimal> result : historyLineResults.entrySet()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(result.getKey());
            series1.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), result.getValue().doubleValue());
        }
        dataset.addSeries(series1);

        return dataset;
    }

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}