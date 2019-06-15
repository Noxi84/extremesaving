package service;

import dto.ResultDto;
import model.AccountModel;
import model.CategoryModel;
import model.DataModel;

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
