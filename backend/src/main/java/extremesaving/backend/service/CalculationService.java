package extremesaving.backend.service;

import extremesaving.backend.dto.ResultDto;
import extremesaving.backend.model.AccountModel;
import extremesaving.backend.model.CategoryModel;
import extremesaving.backend.model.DataModel;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

public interface CalculationService {

    ResultDto getResults(Collection<DataModel> dataModels);

    BigDecimal calculateAverageDaily(ResultDto resultDto, CalculationEnum calculationEnum);

    Map<CategoryModel, BigDecimal> getCategoryResults(Collection<DataModel> dataModels);

    Map<AccountModel, BigDecimal> getAcountResults(Collection<DataModel> dataModels);

    Map<Integer, ResultDto> getMonthlyResults(Collection<DataModel> dataModels);

    Map<Integer, ResultDto> getYearlyResults(Collection<DataModel> dataModels);

    Map<Integer, BigDecimal> getYearPredictions(Collection<DataModel> dataModels);
}
