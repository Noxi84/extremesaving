package extremesaving.service;

import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;

import java.util.Collection;
import java.util.List;

public interface CalculationService {

    ResultDto getResults(Collection<DataModel> dataModels);

    List<DataModel> removeOutliners(Collection<DataModel> dataModels);
}