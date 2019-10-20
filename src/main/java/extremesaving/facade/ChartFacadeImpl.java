package extremesaving.facade;

import extremesaving.service.chart.ChartService;

public class ChartFacadeImpl implements ChartFacade {

    private ChartService monthlyBarChartService;
    private ChartService yearlyBarChartService;
    private ChartService yearLineChartService;
    private ChartService goalLineChartService;

    @Override
    public void generateCharts() {
        monthlyBarChartService.generateChartPng();
        yearlyBarChartService.generateChartPng();
        yearLineChartService.generateChartPng();
        goalLineChartService.generateChartPng();
    }

    public void setMonthlyBarChartService(ChartService monthlyBarChartService) {
        this.monthlyBarChartService = monthlyBarChartService;
    }

    public void setYearlyBarChartService(ChartService yearlyBarChartService) {
        this.yearlyBarChartService = yearlyBarChartService;
    }

    public void setYearLineChartService(ChartService yearLineChartService) {
        this.yearLineChartService = yearLineChartService;
    }

    public void setGoalLineChartService(ChartService goalLineChartService) {
        this.goalLineChartService = goalLineChartService;
    }
}