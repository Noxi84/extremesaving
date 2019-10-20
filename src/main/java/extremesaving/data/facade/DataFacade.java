package extremesaving.data.facade;

import extremesaving.data.dto.DataDto;

import java.util.Collection;
import java.util.List;

public interface DataFacade {

    List<DataDto> findAll();

    String getTipOfTheDay();

    List<DataDto> removeOutliners(Collection<DataDto> dataDtos);

    List<DataDto> filterEstimatedDateRange(Collection<DataDto> dataDtos);
}