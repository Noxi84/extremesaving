package extremesaving.calculation.service;

import extremesaving.calculation.dto.ResultDto;
import extremesaving.data.dto.DataDto;

import java.util.Collection;
import java.util.List;

public interface CalculationService {

    ResultDto getResultDto(Collection<DataDto> dataDtos);

    List<DataDto> filterOutliners(Collection<DataDto> dataDtos);
}