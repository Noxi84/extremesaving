package extremesaving.backend.service;

import extremesaving.backend.dto.ResultDto;
import extremesaving.backend.model.AccountModel;
import extremesaving.backend.model.CategoryModel;
import extremesaving.backend.model.DataModel;
import extremesaving.util.DateUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Component("defaultCalculationService")
public class DefaultCalculationService implements CalculationService {

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

        return resultDto;
    }

    @Override
    public BigDecimal calculateAverageDaily(ResultDto resultDto, CalculationEnum calculationEnum) {
        try {
            BigDecimal amount = CalculationEnum.INCOME.equals(calculationEnum) ? resultDto.getIncomes() : resultDto.getExpenses();
            return amount.divide(BigDecimal.valueOf(resultDto.getDaysSinceLastUpdate()), RoundingMode.HALF_DOWN);
        } catch (ArithmeticException ex) {
            return null;
        }
    }

    @Override
    public Map<CategoryModel, BigDecimal> getCategoryResults(Collection<DataModel> dataModels) {
        Map<CategoryModel, BigDecimal> categoryResults = new HashMap<>();

        for (DataModel dataModel : dataModels) {
            if (!dataModel.getCategory().isTransfer()) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dataModel.getDate());
                BigDecimal categoryResultAmount = categoryResults.get(dataModel.getCategory());
                if (categoryResultAmount == null) {
                    categoryResultAmount = BigDecimal.ZERO;
                }
                categoryResultAmount = categoryResultAmount.add(dataModel.getValue());
                categoryResults.put(dataModel.getCategory(), categoryResultAmount);
            }
        }

        return categoryResults;
    }

    @Override
    public Map<AccountModel, BigDecimal> getAcountResults(Collection<DataModel> dataModels) {
        Map<AccountModel, BigDecimal> accountResults = new HashMap<>();
        for (DataModel dataModel : dataModels) {
            BigDecimal accountResultAmount = accountResults.get(dataModel.getAccount());
            if (accountResultAmount == null) {
                accountResultAmount = BigDecimal.ZERO;
            }
            accountResultAmount = accountResultAmount.add(dataModel.getValue());
            accountResults.put(dataModel.getAccount(), accountResultAmount);
        }
        return accountResults;
    }

    @Override
    public Map<Integer, ResultDto> getMonthlyResults(Collection<DataModel> dataModels) {
        Map<Integer, ResultDto> monthlyResults = new HashMap<>();
        monthlyResults.put(Calendar.JANUARY, new ResultDto());
        monthlyResults.put(Calendar.FEBRUARY, new ResultDto());
        monthlyResults.put(Calendar.MARCH, new ResultDto());
        monthlyResults.put(Calendar.APRIL, new ResultDto());
        monthlyResults.put(Calendar.MAY, new ResultDto());
        monthlyResults.put(Calendar.JUNE, new ResultDto());
        monthlyResults.put(Calendar.JULY, new ResultDto());
        monthlyResults.put(Calendar.AUGUST, new ResultDto());
        monthlyResults.put(Calendar.SEPTEMBER, new ResultDto());
        monthlyResults.put(Calendar.OCTOBER, new ResultDto());
        monthlyResults.put(Calendar.NOVEMBER, new ResultDto());
        monthlyResults.put(Calendar.DECEMBER, new ResultDto());

        for (DataModel dataModel : dataModels) {
            if (!dataModel.getCategory().isTransfer()) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dataModel.getDate());

                ResultDto resultDtoForThisMonth = monthlyResults.get(cal.get(Calendar.MONTH));
                resultDtoForThisMonth.setResult(resultDtoForThisMonth.getResult().add(dataModel.getValue()));

                if (BigDecimal.ZERO.compareTo(dataModel.getValue()) > 0) {
                    resultDtoForThisMonth.setExpenses(resultDtoForThisMonth.getExpenses().add(dataModel.getValue()));
                } else {
                    resultDtoForThisMonth.setIncomes(resultDtoForThisMonth.getIncomes().add(dataModel.getValue()));
                }
            }
        }

        return monthlyResults;
    }

    @Override
    public Map<Integer, ResultDto> getYearlyResults(Collection<DataModel> dataModels) {
        Calendar calendar = Calendar.getInstance();
        Map<Integer, ResultDto> yearlyResults = new HashMap<>();
        yearlyResults.put(calendar.get(Calendar.YEAR) - 11, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 10, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 9, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 8, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 7, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 6, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 5, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 4, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 3, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 2, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 1, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR), new ResultDto());

        for (DataModel dataModel : dataModels) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataModel.getDate());

            ResultDto resultDtoForThisYear = yearlyResults.get(cal.get(Calendar.YEAR));
            resultDtoForThisYear.setResult(resultDtoForThisYear.getResult().add(dataModel.getValue()));

            if (BigDecimal.ZERO.compareTo(dataModel.getValue()) > 0) {
                resultDtoForThisYear.setExpenses(resultDtoForThisYear.getExpenses().add(dataModel.getValue()));
            } else {
                resultDtoForThisYear.setIncomes(resultDtoForThisYear.getIncomes().add(dataModel.getValue()));
            }
        }
        return yearlyResults;
    }

    @Override
    public Map<Integer, BigDecimal> getYearPredictions(Collection<DataModel> dataModels) {
        Map<Integer, BigDecimal> yearPredictions = new HashMap<>();

        ResultDto resultDto = getResults(dataModels.stream().filter(dataModel -> !dataModel.getCategory().isTransfer()).collect(Collectors.toSet()));
        BigDecimal avgDailyIncome = calculateAverageDaily(resultDto, CalculationEnum.INCOME);
        BigDecimal avgDailyExpense = calculateAverageDaily(resultDto, CalculationEnum.EXPENSE);

        for (int yearCounter = 1; yearCounter < 21; yearCounter++) {
            int year = yearCounter + Calendar.getInstance().get(Calendar.YEAR);
            Calendar futureYear = Calendar.getInstance();
            futureYear.add(Calendar.YEAR, yearCounter);
            long totalDaysUntilFutureYear = DateUtils.daysBetween(futureYear.getTime(), new Date());
            BigDecimal futureYearResult = avgDailyIncome.multiply(BigDecimal.valueOf(totalDaysUntilFutureYear)).add(avgDailyExpense.multiply(BigDecimal.valueOf((totalDaysUntilFutureYear))));
            yearPredictions.put(year, resultDto.getResult().add(futureYearResult));
        }
        return yearPredictions;
    }
}
