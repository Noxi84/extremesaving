package extremesaving.charts.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import extremesaving.data.dto.MiniResultDto;

public interface ChartDataService {

    Map<Date, MiniResultDto> getMonthResults();

    Map<Date, BigDecimal> getGoalLineHistoryResults();

    Map<Date, BigDecimal> getGoalLineFutureEstimationResults();

    Map<Date, BigDecimal> getGoalLineSurvivalEstimationResults();
}