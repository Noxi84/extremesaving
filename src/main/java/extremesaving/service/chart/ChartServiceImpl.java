package extremesaving.service.chart;

public class ChartServiceImpl implements ChartService {

    private ChartGenerator accountPieChartGenerator;
    private ChartGenerator monthlyBarChartGenerator;
    private ChartGenerator yearlyBarChartGenerator;
    private ChartGenerator historyLineChartGenerator;
    private ChartGenerator futureLineChartGenerator;

    @Override
    public void generateCharts() {
        accountPieChartGenerator.generateChartPng();
        monthlyBarChartGenerator.generateChartPng();
        yearlyBarChartGenerator.generateChartPng();
        historyLineChartGenerator.generateChartPng();
        futureLineChartGenerator.generateChartPng();
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

    public void setHistoryLineChartGenerator(ChartGenerator historyLineChartGenerator) {
        this.historyLineChartGenerator = historyLineChartGenerator;
    }

    public void setFutureLineChartGenerator(ChartGenerator futureLineChartGenerator) {
        this.futureLineChartGenerator = futureLineChartGenerator;
    }
}