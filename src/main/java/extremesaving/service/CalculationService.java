package extremesaving.service;

import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

public interface CalculationService {

    ResultDto getResults(Collection<DataModel> dataModels);

    BigDecimal calculateAverageDaily(ResultDto resultDto, CalculationEnum calculationEnum);

//    Map<CategoryModel, BigDecimal> getCategoryResults(Collection<DataModel> dataModels);

//    Map<AccountModel, BigDecimal> getAcountResults(Collection<DataModel> dataModels);

    Map<Integer, BigDecimal> getYearPredictions(Collection<DataModel> dataModels);
}
