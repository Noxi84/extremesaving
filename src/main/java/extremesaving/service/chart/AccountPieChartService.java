package extremesaving.service.chart;

import extremesaving.service.ChartDataService;
import extremesaving.util.ChartUtils;
import extremesaving.util.PropertiesValueHolder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.math.BigDecimal;
import java.util.Map;

import static extremesaving.util.PropertyValueENum.ACCOUNT_PIE_CHART_IMAGE_FILE;

public class AccountPieChartService implements ChartService {

    private ChartDataService chartDataService;

    @Override
    public void generateChartPng() {
        JFreeChart chart = ChartFactory.createPieChart("Accounts", createDataSet(), true, false, false);
        chart.setTitle("");
        ChartUtils.writeChartPng(chart, PropertiesValueHolder.getInstance().getPropValue(ACCOUNT_PIE_CHART_IMAGE_FILE), 550, 300);
    }

    private DefaultPieDataset createDataSet() {
        Map<String, BigDecimal> accounts = chartDataService.getAccountResults();

        DefaultPieDataset dataSet = new DefaultPieDataset();
        for (Map.Entry<String, BigDecimal> account : accounts.entrySet()) {
            dataSet.setValue(account.getKey(), account.getValue());
        }
        return dataSet;
    }

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}