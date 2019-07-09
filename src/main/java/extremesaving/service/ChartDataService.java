package extremesaving.service;

import extremesaving.dto.MiniResultDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface ChartDataService {

    Map<String, BigDecimal> getAccountResults();

    Map<Integer, MiniResultDto> getMonthlyResults();

    Map<Integer, MiniResultDto> getYearlyResults();

    Map<Date, BigDecimal> getHistoryLineResults();

    Map<Date, BigDecimal> getGoalLineResults();
}