package extremesaving.service.chart;

public class ChartServiceImpl implements ChartService {

    private ChartGenerator accountPieChartGenerator;
    private ChartGenerator monthlyBarChartGenerator;
    private ChartGenerator yearlyBarChartGenerator;
    private ChartGenerator overallLineChartGenerator;

    @Override
    public void generateCharts() {
        accountPieChartGenerator.generateChartPng();
        monthlyBarChartGenerator.generateChartPng();
        yearlyBarChartGenerator.generateChartPng();
//        overallLineChartGenerator.generateChartPng(totalsDto);
    }

    public void setAccountPieChartGenerator(ChartGenerator accountPieChartGenerator) {
        this.accountPieChartGenerator = accountPieChartGenerator;
    }

    public void setMonthlyBarChartGenerator(ChartGenerator monthlyBarChartGenerator) {
        this.monthlyBarChartGenerator = monthlyBarChartGenerator;
    }

    public void setYearlyBarChartGenerator(ChartGenerator yearlyBarChartGenerator) {
        this.yearlyBarChartGenerator = yearlyBarChartGenerator;
    }

    public void setOverallLineChartGenerator(ChartGenerator overallLineChartGenerator) {
        this.overallLineChartGenerator = overallLineChartGenerator;
    }

}
