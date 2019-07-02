package extremesaving.service;

import extremesaving.dto.ResultDto;

import java.util.Map;

public interface ChartDataService {

    Map<Integer, ResultDto> getMonthlyResults();

    Map<Integer, ResultDto> getYearlyResults();
}