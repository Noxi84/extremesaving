package extremesaving.data.facade;

import extremesaving.calculation.dto.MiniResultDto;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.data.dto.DataDto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DataFacade {

    List<DataDto> findAll();

    Date getLastItemAdded();

    BigDecimal getTotalBalance();

    Date getBestMonth();

    Date getWorstMonth();

    Date getBestYear();

    Date getWorstYear();

    Map<Integer, MiniResultDto> getMonthlyResults(Collection<DataDto> dataDtos);

    Map<Integer, MiniResultDto> getYearlyResults(Collection<DataDto> dataDtos);

    List<ResultDto> getMostProfitableItems(Collection<DataDto> dataDtos);

    List<ResultDto> getMostExpensiveItems(Collection<DataDto> dataDtos);

    String getTipOfTheDay();

    List<DataDto> removeOutliners(Collection<DataDto> dataDtos);

    List<DataDto> filterEstimatedDateRange(Collection<DataDto> dataDtos);
}