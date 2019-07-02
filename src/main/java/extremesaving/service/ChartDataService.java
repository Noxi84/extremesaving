package extremesaving.service;

import extremesaving.dto.ResultDto;

import java.math.BigDecimal;
import java.util.Map;

public interface ChartDataService {

    Map<String, BigDecimal> getAccountResults();

    Map<Integer, ResultDto> getMonthlyResults();

    Map<Integer, ResultDto> getYearlyResults();
}