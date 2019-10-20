package extremesaving.data.facade;

import extremesaving.data.dto.DataDto;

import java.util.List;

public interface DataFacade {

    List<DataDto> findAll();

    String getTipOfTheDay();
}