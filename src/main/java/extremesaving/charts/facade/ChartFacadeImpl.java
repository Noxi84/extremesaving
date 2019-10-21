package extremesaving.charts.facade;

import extremesaving.charts.service.ChartService;

public class ChartFacadeImpl implements ChartFacade {

    private ChartService monthlyBarChartService;
    private ChartService yearlyBarChartService;
    private ChartService yearLineChartService;
    private ChartService goalLineChartService;

    @Override
    public void generateMonthlyBarChart() {
        System.out.println("Generating MonthlyBarChart...");
        monthlyBarChartService.generateChartPng();
    }

    @Override
    public void generateYearlyBarChart() {
        System.out.println("Generating YearlyBarChart...");
        yearlyBarChartService.generateChartPng();
    }

    @Override
    public void generateYearLineChart() {
        System.out.println("Generating YearLineChart...");
        yearLineChartService.generateChartPng();
    }

    @Override
    public void generateGoalLineChart() {
        System.out.println("Generating GoalLineChart...");
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