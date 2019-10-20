package extremesaving.data.service;

import extremesaving.data.dto.DataDto;
import extremesaving.data.model.DataModel;

import java.util.Collection;
import java.util.List;

public interface DataService {

    List<DataModel> findAll();

    List<DataDto> filterOutliners(Collection<DataDto> dataDtos);
}