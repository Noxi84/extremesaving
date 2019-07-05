package extremesaving.service;

import extremesaving.dto.MiniResultDto;
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

    Map<Integer, MiniResultDto> getMonthlyResults(Collection<DataModel> dataModels);

    Map<Integer, MiniResultDto> getYearlyResults(Collection<DataModel> dataModels);

    List<DataModel> getMostProfitableItems(Collection<DataModel> dataModels);

    List<DataModel> getMostExpensiveItems(Collection<DataModel> dataModels);
}