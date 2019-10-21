package extremesaving.charts.service;

import extremesaving.calculation.dto.MiniResultDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface ChartDataService {

    Map<Integer, MiniResultDto> getMonthResults();

    Map<Integer, MiniResultDto> getYearResults();

    Map<Date, BigDecimal> getGoalLineHistoryResults();

    Map<Date, BigDecimal> getGoalLineFutureEstimationResults();

    Map<Date, BigDecimal> getGoalLineSurvivalEstimationResults();
}