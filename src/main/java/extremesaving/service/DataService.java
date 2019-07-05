package extremesaving.service;

import extremesaving.dto.CategoryDto;
import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DataService {

    List<DataModel> findAll();

    Date getLastItemAdded();

    long getTotalItems();

    BigDecimal getTotalBalance();

    Date getBestMonth();

    Date getWorstMonth();

    Date getBestYear();

    Date getWorstYear();

    Map<Integer, ResultDto> getMonthlyResults(Collection<DataModel> dataModels);

    Map<Integer, ResultDto> getYearlyResults(Collection<DataModel> dataModels);

    List<CategoryDto> getMostProfitableItems(Collection<DataModel> dataModels);

    List<CategoryDto> getMostExpensiveItems(Collection<DataModel> dataModels);
}