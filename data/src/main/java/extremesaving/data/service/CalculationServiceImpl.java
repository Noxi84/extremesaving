package extremesaving.data.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import extremesaving.data.dto.CategoryDto;
import extremesaving.data.dto.ResultDto;
import extremesaving.data.util.NumberUtils;
import extremesaving.data.dto.DataDto;

public class CalculationServiceImpl implements CalculationService {

    @Override
    public ResultDto getResultDto(Collection<DataDto> dataDtos) {
        ResultDto resultDto = getResultDto(dataDtos, true);
        resultDto.setData(new HashSet<>(dataDtos));
        return resultDto;
    }

    private ResultDto getResultDto(Collection<DataDto> dataDtos, boolean savingRatio) {
        ResultDto resultDto = new ResultDto();
        resultDto.setData(new HashSet<>(dataDtos));
        if (savingRatio) {
            resultDto.setSavingRatio(getSavingRatio(dataDtos));
        }
        for (DataDto dataDto : dataDtos) {
            enrichResultDto(resultDto, dataDto.getDate(), dataDto.getValue());
        }
        return resultDto;
    }

    @Override
    public ResultDto getResultDto(Map<Date, BigDecimal> dataMap) {
        ResultDto resultDto = new ResultDto();
        for (Map.Entry<Date, BigDecimal> data : dataMap.entrySet()) {
            Date date = data.getKey();
            BigDecimal value = data.getValue();
            enrichResultDto(resultDto, date, value);
        }
        return resultDto;
    }

    private void enrichResultDto(ResultDto resultDto, Date date, BigDecimal value) {
        resultDto.setResult(resultDto.getResult().add(value));
        resultDto.setNumberOfItems(resultDto.getNumberOfItems() + 1);
        if (resultDto.getHighestResult().compareTo(value) > 0) {
            resultDto.setHighestResult(value);
        }
        if (NumberUtils.isExpense(value)) {
            resultDto.setNumberOfExpenses(resultDto.getNumberOfExpenses() + 1);
            resultDto.setExpenses(resultDto.getExpenses().add(value));
            if (resultDto.getHighestExpense().compareTo(value) > 0) {
                resultDto.setHighestExpense(value);
            }
        } else {
            resultDto.setNumberOfIncomes(resultDto.getNumberOfIncomes() + 1);
            resultDto.setIncomes(resultDto.getIncomes().add(value));
            if (resultDto.getHighestIncome().compareTo(value) < 0) {
                resultDto.setHighestIncome(value);
            }
        }

        if (resultDto.getFirstDate() == null || date.before(resultDto.getFirstDate())) {
            resultDto.setFirstDate(date);
        }
        if (resultDto.getLastDate() == null || date.after(resultDto.getLastDate())) {
            resultDto.setLastDate(date);
        }
    }

    protected BigDecimal getSavingRatio(Collection<DataDto> dataDtos) {
        List<CategoryDto> profitResults = getMostProfitableCategories(dataDtos);
        List<CategoryDto> expensesResults = getMostExpensiveCategories(dataDtos);
        return calculateSavingRatio(profitResults, expensesResults);
    }

    protected List<CategoryDto> getMostProfitableCategories(Collection<DataDto> dataDtos) {
        List<String> categories = new ArrayList<>(dataDtos.stream().map(dataDto -> dataDto.getCategory()).collect(Collectors.toSet()));
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (String category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(category);
            categoryDto.setTotalResults(getResultDto(dataDtos.stream().filter(dataDto -> dataDto.getCategory().equals(category)).collect(Collectors.toList()), false));
            if (NumberUtils.isIncome(categoryDto.getTotalResults().getResult())) {
                categoryDtos.add(categoryDto);
            }
        }
        Collections.sort(categoryDtos, (o1, o2) -> o2.getTotalResults().getResult().compareTo(o1.getTotalResults().getResult()));
        return categoryDtos;
    }

    protected List<CategoryDto> getMostExpensiveCategories(Collection<DataDto> dataDtos) {
        List<String> categories = new ArrayList<>(dataDtos.stream().map(dataDto -> dataDto.getCategory()).collect(Collectors.toSet()));
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (String category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(category);
            categoryDto.setTotalResults(getResultDto(dataDtos.stream().filter(dataDto -> dataDto.getCategory().equals(category)).collect(Collectors.toList()), false));
            if (NumberUtils.isExpense(categoryDto.getTotalResults().getResult())) {
                categoryDtos.add(categoryDto);
            }
        }
        Collections.sort(categoryDtos, Comparator.comparing(o -> o.getTotalResults().getResult()));
        return categoryDtos;
    }

    protected BigDecimal calculateSavingRatio(List<CategoryDto> profitResults, List<CategoryDto> expensesResults) {
        BigDecimal profitAmount = profitResults.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expensesAmount = expensesResults.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expensesAmountReversed = expensesAmount.multiply(BigDecimal.valueOf(-1));
        if (BigDecimal.ZERO.compareTo(expensesAmountReversed) == 0) {
            return BigDecimal.valueOf(100);
        } else if (profitAmount.compareTo(expensesAmountReversed) > 0) {
            return BigDecimal.valueOf(100).subtract(expensesAmountReversed.divide(profitAmount, 2, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100)));
        }
        return BigDecimal.ZERO;
    }
}