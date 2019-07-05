package extremesaving.service;

import extremesaving.dto.MiniResultDto;

import java.math.BigDecimal;
import java.util.Map;

public interface ChartDataService {

    Map<String, BigDecimal> getAccountResults();

    Map<Integer, MiniResultDto> getMonthlyResults();

    Map<Integer, MiniResultDto> getYearlyResults();

    Map<Integer, BigDecimal> getYearPredictions();
}