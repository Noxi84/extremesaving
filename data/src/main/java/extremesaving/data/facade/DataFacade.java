package extremesaving.data.facade;

import java.util.List;

import extremesaving.data.dto.DataDto;

public interface DataFacade {

    List<DataDto> findAll();
}