package extremesaving.calculation.facade;

import extremesaving.calculation.dto.CategoryDto;
import extremesaving.calculation.dto.MiniResultDto;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.data.dto.DataDto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CalculationFacade {

    ResultDto getResults(Collection<DataDto> dataDtos);

    Map<Integer, MiniResultDto> getMonthResults(Collection<DataDto> dataDtos);

    Map<Integer, MiniResultDto> getYearResults(Collection<DataDto> dataDtos);

    List<ResultDto> getMostProfitableItems(Collection<DataDto> dataDtos);

    List<ResultDto> getMostExpensiveItems(Collection<DataDto> dataDtos);

    BigDecimal calculateSavingRatio(List<CategoryDto> profitResults, List<CategoryDto> expensesResults);
}