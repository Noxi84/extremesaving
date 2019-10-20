package extremesaving.calculation.service;

import extremesaving.calculation.dto.ResultDto;
import extremesaving.data.dto.DataDto;

import java.util.Collection;

public interface CalculationService {

    ResultDto getResultDto(Collection<DataDto> dataDtos);
}