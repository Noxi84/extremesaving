package extremesaving.calculation.service;

import extremesaving.calculation.dto.ResultDto;
import extremesaving.data.dto.DataDto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface CalculationService {

    Map<Date, BigDecimal> combineDays(Collection<DataDto> dataDtos);

    ResultDto getResultDto(Collection<DataDto> dataDtos);

    ResultDto getResultDto(Map<Date, BigDecimal> dataMap);

    Map<Date, BigDecimal> removeOutliners(Map<Date, BigDecimal> dataMap);

    Map<Date, BigDecimal> filterEstimatedDateRange(Map<Date, BigDecimal> dataMap);
}