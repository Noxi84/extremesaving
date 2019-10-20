package extremesaving.calculation.service;

import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.enums.CalculationEnum;
import extremesaving.data.dto.DataDto;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class CalculationServiceImpl implements CalculationService {

    @Override
    public ResultDto getResultDto(Collection<DataDto> dataDtos) {
        ResultDto resultDto = new ResultDto();
        resultDto.setData(new HashSet<>(dataDtos));

        for (DataDto dataDto : dataDtos) {
            resultDto.setResult(resultDto.getResult().add(dataDto.getValue()));
            resultDto.setNumberOfItems(resultDto.getNumberOfItems() + 1);
            if (resultDto.getHighestResult().compareTo(dataDto.getValue()) > 0) {
                resultDto.setHighestResult(dataDto.getValue());
            }
            if (NumberUtils.isExpense(dataDto.getValue())) {
                resultDto.setNumberOfExpenses(resultDto.getNumberOfExpenses() + 1);
                resultDto.setExpenses(resultDto.getExpenses().add(dataDto.getValue()));
                if (resultDto.getHighestExpense().compareTo(dataDto.getValue()) > 0) {
                    resultDto.setHighestExpense(dataDto.getValue());
                }
            } else {
                resultDto.setNumberOfIncomes(resultDto.getNumberOfIncomes() + 1);
                resultDto.setIncomes(resultDto.getIncomes().add(dataDto.getValue()));
                if (resultDto.getHighestIncome().compareTo(dataDto.getValue()) < 0) {
                    resultDto.setHighestIncome(dataDto.getValue());
                }
            }

            if (resultDto.getFirstDate() == null || dataDto.getDate().before(resultDto.getFirstDate())) {
                resultDto.setFirstDate(dataDto.getDate());
            }
            if (resultDto.getLastDate() == null || dataDto.getDate().after(resultDto.getLastDate())) {
                resultDto.setLastDate(dataDto.getDate());
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
                return amount.divide(BigDecimal.valueOf(daysBetween), 2, RoundingMode.HALF_DOWN);
            }
            return BigDecimal.ZERO;
        } catch (ArithmeticException ex) {
            return null;
        }
    }
}