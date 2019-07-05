package extremesaving.service;

import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;

import java.util.Collection;

public interface CalculationService {

    ResultDto getResults(Collection<DataModel> dataModels);
}