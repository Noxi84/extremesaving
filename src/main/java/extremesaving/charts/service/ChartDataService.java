package extremesaving.charts.service;

import extremesaving.calculation.dto.MiniResultDto;
import extremesaving.charts.GoalLineResultEnum;
import org.jfree.chart.JFreeChart;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface ChartDataService {

    Map<Integer, MiniResultDto> getMonthlyResults();

    Map<Integer, MiniResultDto> getYearlyResults();

    Map<Date, BigDecimal> getGoalLineResults(GoalLineResultEnum goalLineResultEnum);

    void writeChartPng(JFreeChart chart, String file, int width, int height);
}