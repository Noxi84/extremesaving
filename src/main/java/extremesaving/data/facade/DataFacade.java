package extremesaving.data.facade;

import extremesaving.calculation.dto.MiniResultDto;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.data.model.DataModel;
import extremesaving.data.model.TipOfTheDayModel;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DataFacade {

    Date getLastItemAdded();

    BigDecimal getTotalBalance();

    Date getBestMonth();

    Date getWorstMonth();

    Date getBestYear();

    Date getWorstYear();

    Map<Integer, MiniResultDto> getMonthlyResults(Collection<DataModel> dataModels);

    Map<Integer, MiniResultDto> getYearlyResults(Collection<DataModel> dataModels);

    List<ResultDto> getMostProfitableItems(Collection<DataModel> dataModels);

    List<ResultDto> getMostExpensiveItems(Collection<DataModel> dataModels);

    List<TipOfTheDayModel> getTipOfTheDays();
}
