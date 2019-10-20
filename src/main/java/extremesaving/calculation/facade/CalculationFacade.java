package extremesaving.calculation.facade;

import extremesaving.calculation.dto.MiniResultDto;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.data.dto.DataDto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CalculationFacade {

    ResultDto getResults(Collection<DataDto> dataDtos);

    ResultDto getResultDto(Collection<DataDto> dataDtos);

    Long getSurvivalDays();

    BigDecimal getPreviousGoal();

    BigDecimal getCurrentGoal();

    int getGoalIndex(BigDecimal goalAmount);

    /**
     * @return The next goal based on the index. Default index use should be 1 for the next upcoming goal.
     */
    BigDecimal getNextGoal(int index);

    Long getGoalTime(BigDecimal goal);

    Date getGoalReachedDate(BigDecimal goal);

    Map<Integer, MiniResultDto> getMonthlyResults(Collection<DataDto> dataDtos);

    Map<Integer, MiniResultDto> getYearlyResults(Collection<DataDto> dataDtos);

    List<ResultDto> getMostProfitableItems(Collection<DataDto> dataDtos);

    List<ResultDto> getMostExpensiveItems(Collection<DataDto> dataDtos);

    Date getLastItemAdded();

    BigDecimal getTotalBalance();

    Date getBestMonth();

    Date getWorstMonth();

    Date getBestYear();

    Date getWorstYear();
}