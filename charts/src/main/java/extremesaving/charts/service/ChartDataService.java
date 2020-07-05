package extremesaving.charts.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import extremesaving.data.dto.MiniResultDto;

public interface ChartDataService {

    Map<Date, MiniResultDto> getMonthResults();

    Map<Date, BigDecimal> getOverallLineHistoryResults();

    Map<Date, BigDecimal> getOverallLineFutureEstimationResults();

    Map<Date, BigDecimal> getOverallLineSurvivalEstimationResults();
}