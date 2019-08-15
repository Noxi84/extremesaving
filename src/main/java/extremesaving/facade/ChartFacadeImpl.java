package extremesaving.facade;

import extremesaving.service.chart.ChartService;

public class ChartFacadeImpl implements ChartFacade {

    private ChartService accountPieChartService;
    private ChartService monthlyBarChartService;
    private ChartService yearlyBarChartService;
    private ChartService monthLineChartService;
    private ChartService goalLineChartService;

    @Override
    public void generateCharts() {
        accountPieChartService.generateChartPng();
        monthlyBarChartService.generateChartPng();
        yearlyBarChartService.generateChartPng();
        monthLineChartService.generateChartPng();
        goalLineChartService.generateChartPng();
    }

    public void setAccountPieChartService(ChartService accountPieChartService) {
        this.accountPieChartService = accountPieChartService;
    }

    public void setMonthlyBarChartService(ChartService monthlyBarChartService) {
        this.monthlyBarChartService = monthlyBarChartService;
    }

    public void setYearlyBarChartService(ChartService yearlyBarChartService) {
        this.yearlyBarChartService = yearlyBarChartService;
    }

    public void setMonthLineChartService(ChartService monthLineChartService) {
        this.monthLineChartService = monthLineChartService;
    }

    public void setGoalLineChartService(ChartService goalLineChartService) {
        this.goalLineChartService = goalLineChartService;
    }
}