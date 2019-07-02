package extremesaving.service;

import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.util.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

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

//    @Override
//    public Map<CategoryModel, BigDecimal> getCategoryResults(Collection<DataModel> dataModels) {
//        Map<CategoryModel, BigDecimal> categoryResults = new HashMap<>();
//
//        for (DataModel dataModel : dataModels) {
//            if (!dataModel.getCategory().isTransfer()) {
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(dataModel.getDate());
//                BigDecimal categoryResultAmount = categoryResults.get(dataModel.getCategory());
//                if (categoryResultAmount == null) {
//                    categoryResultAmount = BigDecimal.ZERO;
//                }
//                categoryResultAmount = categoryResultAmount.add(dataModel.getValue());
//                categoryResults.put(dataModel.getCategory(), categoryResultAmount);
//            }
//        }
//
//        return categoryResults;
//    }
//
//    @Override
//    public Map<AccountModel, BigDecimal> getAcountResults(Collection<DataModel> dataModels) {
//        Map<AccountModel, BigDecimal> accountResults = new HashMap<>();
//        for (DataModel dataModel : dataModels) {
//            BigDecimal accountResultAmount = accountResults.get(dataModel.getAccount());
//            if (accountResultAmount == null) {
//                accountResultAmount = BigDecimal.ZERO;
//            }
//            accountResultAmount = accountResultAmount.add(dataModel.getValue());
//            accountResults.put(dataModel.getAccount(), accountResultAmount);
//        }
//        return accountResults;
//    }


    @Override
    public Map<Integer, BigDecimal> getYearPredictions(Collection<DataModel> dataModels) {
        Map<Integer, BigDecimal> yearPredictions = new HashMap<>();

//        ResultDto resultDto = getResults(dataModels.stream().filter(dataModel -> !dataModel.getCategory().isTransfer()).collect(Collectors.toSet()));
        ResultDto resultDto = getResults(dataModels.stream().collect(Collectors.toSet()));
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
