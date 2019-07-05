package extremesaving.service;

import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.util.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class CalculationServiceImpl implements CalculationService {

    @Override
    public ResultDto getResults(Collection<DataModel> dataModels) {
        ResultDto resultDto = new ResultDto();
        resultDto.setData(new HashSet<>(dataModels));

        for (DataModel dataModel : dataModels) {
            resultDto.setResult(resultDto.getResult().add(dataModel.getValue()));
            resultDto.setNumberOfItems(resultDto.getNumberOfItems() + 1);
            if (resultDto.getHighestResult().compareTo(dataModel.getValue()) > 0) {
                resultDto.setHighestResult(dataModel.getValue());
            }
            if (BigDecimal.ZERO.compareTo(dataModel.getValue()) > 0) {
                resultDto.setNumberOfExpenses(resultDto.getNumberOfExpenses() + 1);
                resultDto.setExpenses(resultDto.getExpenses().add(dataModel.getValue()));
                if (resultDto.getHighestExpense().compareTo(dataModel.getValue()) > 0) {
                    resultDto.setHighestExpense(dataModel.getValue());
                }
            } else {
                resultDto.setNumberOfIncomes(resultDto.getNumberOfIncomes() + 1);
                resultDto.setIncomes(resultDto.getIncomes().add(dataModel.getValue()));
                if (resultDto.getHighestIncome().compareTo(dataModel.getValue()) < 0) {
                    resultDto.setHighestIncome(dataModel.getValue());
                }
            }

            if (resultDto.getFirstDate() == null || dataModel.getDate().before(resultDto.getFirstDate())) {
                resultDto.setFirstDate(dataModel.getDate());
            }
            if (resultDto.getLastDate() == null || dataModel.getDate().after(resultDto.getLastDate())) {
                resultDto.setLastDate(dataModel.getDate());
            }
        }
        if (resultDto.getFirstDate() != null) {
            resultDto.setDaysSinceLastUpdate(DateUtils.daysBetween(new Date(), resultDto.getFirstDate()));
        }

        resultDto.setAverageDailyIncome(calculateAverageDaily(resultDto, CalculationEnum.INCOME));
        resultDto.setAverageDailyExpense(calculateAverageDaily(resultDto, CalculationEnum.EXPENSE));
        resultDto.setAverageDailyResult(calculateAverageDaily(resultDto, CalculationEnum.RESULT));

        return resultDto;
    }

    protected BigDecimal calculateAverageDaily(ResultDto resultDto, CalculationEnum calculationEnum) {
        try {
            if (resultDto.getFirstDate() != null) {
                BigDecimal amount = null;
                if (CalculationEnum.INCOME.equals(calculationEnum)) {
                    amount = resultDto.getIncomes();
                } else if (CalculationEnum.EXPENSE.equals(calculationEnum)) {
                    amount = resultDto.getExpenses();
                } else if (CalculationEnum.RESULT.equals(calculationEnum)) {
                    amount = resultDto.getResult();
                }

                long daysBetween = DateUtils.daysBetween(new Date(), resultDto.getFirstDate());
                return amount.divide(BigDecimal.valueOf(daysBetween), RoundingMode.HALF_DOWN);
            }
            return BigDecimal.ZERO;
        } catch (ArithmeticException ex) {
            return null;
        }
    }
}