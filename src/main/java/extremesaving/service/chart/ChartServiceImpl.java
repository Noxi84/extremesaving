package extremesaving.service.chart;

import extremesaving.chart.ChartGenerator;

public class ChartServiceImpl implements ChartService {

    private ChartGenerator accountPieChartGenerator;
    private ChartGenerator monthlyBarChartGenerator;
    private ChartGenerator yearlyBarChartGenerator;
    private ChartGenerator overallLineChartGenerator;
    private ChartGenerator monthlyMeterChartGenerator;

    @Override
    public void generateCharts() {
        accountPieChartGenerator.generateChartPng();
        monthlyBarChartGenerator.generateChartPng();
        yearlyBarChartGenerator.generateChartPng();
//        overallLineChartGenerator.generateChartPng(totalsDto);
//        monthlyMeterChartGenerator.generateChartPng(totalsDto);
    }
}
