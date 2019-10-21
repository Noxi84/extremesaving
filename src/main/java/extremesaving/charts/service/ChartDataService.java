package extremesaving.charts.service;

import extremesaving.calculation.dto.MiniResultDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface ChartDataService {

    Map<Integer, MiniResultDto> getMonthlyResults();

    Map<Integer, MiniResultDto> getYearlyResults();

    Map<Date, BigDecimal> getGoalLineHistoryResults();

    Map<Date, BigDecimal> getGoalLineFutureEstimationResults();

    Map<Date, BigDecimal> getGoalLineSurvivalEstimationResults();
}