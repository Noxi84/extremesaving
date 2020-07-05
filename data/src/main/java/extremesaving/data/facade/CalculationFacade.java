package extremesaving.data.facade;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import extremesaving.data.dto.DataDto;
import extremesaving.data.dto.MiniResultDto;
import extremesaving.data.dto.ResultDto;

public interface CalculationFacade {

    ResultDto getResults(Collection<DataDto> dataDtos);

    Map<Date, MiniResultDto> getMonthResults(Collection<DataDto> dataDtos);
}