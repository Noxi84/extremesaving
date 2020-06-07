package extremesaving.data.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import extremesaving.data.dto.ResultDto;
import extremesaving.data.dto.DataDto;

public interface CalculationService {

    ResultDto getResultDto(Collection<DataDto> dataDtos);

    ResultDto getResultDto(Map<Date, BigDecimal> dataMap);
}