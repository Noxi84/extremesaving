package extremesaving.calculation.facade;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import extremesaving.calculation.dto.MiniResultDto;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.data.dto.DataDto;

public interface CalculationFacade {

    ResultDto getResults(Collection<DataDto> dataDtos);

    Map<Date, MiniResultDto> getMonthResults(Collection<DataDto> dataDtos);

    Map<Integer, MiniResultDto> getYearResults(Collection<DataDto> dataDtos);
}