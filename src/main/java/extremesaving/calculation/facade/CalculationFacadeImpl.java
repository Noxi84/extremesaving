package extremesaving.calculation.facade;

import extremesaving.calculation.dto.CategoryDto;
import extremesaving.calculation.dto.MiniResultDto;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.service.CalculationService;
import extremesaving.calculation.util.NumberUtils;
import extremesaving.data.dto.DataDto;
import extremesaving.util.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CalculationFacadeImpl implements CalculationFacade {

    private static Map<Integer, ResultDto> calculationCash = new HashMap<>();

    private CalculationService calculationService;

    @Override
    public ResultDto getResults(Collection<DataDto> dataDtos) {
        int hashCode = Objects.hash(dataDtos.toArray());
        ResultDto result = calculationCash.get(hashCode);
        if (result == null) {
            result = calculationService.getResultDto(dataDtos);
            calculationCash.put(hashCode, result);
        }
        return result;
    }

    @Override
    public List<ResultDto> getMostProfitableItems(Collection<DataDto> dataDtos) {
        List<ResultDto> categoryDescriptionGrouped = createCategoryDescriptionMap(dataDtos);
        return categoryDescriptionGrouped.stream()
                .filter(resultDto -> NumberUtils.isIncome(resultDto.getResult()))
                .sorted((o1, o2) -> o2.getResult().compareTo(o1.getResult()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResultDto> getMostExpensiveItems(Collection<DataDto> dataDtos) {
        List<ResultDto> categoryDescriptionGrouped = createCategoryDescriptionMap(dataDtos);
        return categoryDescriptionGrouped.stream()
                .filter(resultDto -> NumberUtils.isExpense(resultDto.getResult()))
                .sorted(Comparator.comparing(ResultDto::getResult))
                .collect(Collectors.toList());
    }

    protected List<ResultDto> createCategoryDescriptionMap(Collection<DataDto> dataDtos) {
        // Group datamodels for each category + description
        Map<String, List<DataDto>> categoryDescriptionDtos = new HashMap<>();
        for (DataDto dataDto : dataDtos) {
            if (StringUtils.isNotBlank(dataDto.getDescription())) {
                String categoryDescription = dataDto.getCategory().toLowerCase() + "_" + dataDto.getDescription().toLowerCase();
                List<DataDto> dataDtosForCategoryDescription = categoryDescriptionDtos.get(categoryDescription);
                if (dataDtosForCategoryDescription == null) {
                    dataDtosForCategoryDescription = new ArrayList<>();
                }
                dataDtosForCategoryDescription.add(dataDto);
                categoryDescriptionDtos.put(categoryDescription, dataDtosForCategoryDescription);
            }
        }

        // Create ResultDto map
        List<ResultDto> results = new ArrayList<>();
        for (Map.Entry<String, List<DataDto>> categoryDescriptionEntry : categoryDescriptionDtos.entrySet()) {
            results.add(getResults(categoryDescriptionEntry.getValue()));
        }
        return results;
    }

    @Override
    public Map<Integer, MiniResultDto> getMonthResults(Collection<DataDto> dataDtos) {
        Map<Integer, MiniResultDto> monthResults = new HashMap<>();
        monthResults.put(Calendar.JANUARY, new MiniResultDto());
        monthResults.put(Calendar.FEBRUARY, new MiniResultDto());
        monthResults.put(Calendar.MARCH, new MiniResultDto());
        monthResults.put(Calendar.APRIL, new MiniResultDto());
        monthResults.put(Calendar.MAY, new MiniResultDto());
        monthResults.put(Calendar.JUNE, new MiniResultDto());
        monthResults.put(Calendar.JULY, new MiniResultDto());
        monthResults.put(Calendar.AUGUST, new MiniResultDto());
        monthResults.put(Calendar.SEPTEMBER, new MiniResultDto());
        monthResults.put(Calendar.OCTOBER, new MiniResultDto());
        monthResults.put(Calendar.NOVEMBER, new MiniResultDto());
        monthResults.put(Calendar.DECEMBER, new MiniResultDto());

        List<DataDto> filteredDataDtos = dataDtos.stream()
                .filter(dataModel -> DateUtils.equalYears(dataModel.getDate(), new Date()))
                .filter(dataModel -> !dataModel.getCategory().equalsIgnoreCase("..."))
                .collect(Collectors.toList());

        for (DataDto dataDto : filteredDataDtos) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataDto.getDate());

            MiniResultDto resultDtoForThisMonth = monthResults.get(cal.get(Calendar.MONTH));
            resultDtoForThisMonth.setResult(resultDtoForThisMonth.getResult().add(dataDto.getValue()));

            if (NumberUtils.isExpense(dataDto.getValue())) {
                resultDtoForThisMonth.setExpenses(resultDtoForThisMonth.getExpenses().add(dataDto.getValue()));
            } else {
                resultDtoForThisMonth.setIncomes(resultDtoForThisMonth.getIncomes().add(dataDto.getValue()));
            }
        }
        return monthResults;
    }

    @Override
    public Map<Integer, MiniResultDto> getYearResults(Collection<DataDto> dataDtos) {
        Map<Integer, MiniResultDto> yearResults = new HashMap<>();
        List<DataDto> filteredDataDtos = dataDtos.stream().filter(dataModel -> !dataModel.getCategory().equalsIgnoreCase("...")).collect(Collectors.toList());

        for (DataDto dataDto : filteredDataDtos) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataDto.getDate());
            int year = cal.get(Calendar.YEAR);

            MiniResultDto resultDtoForThisYear = yearResults.get(year);
            if (resultDtoForThisYear == null) {
                resultDtoForThisYear = new MiniResultDto();
            }
            resultDtoForThisYear.setResult(resultDtoForThisYear.getResult().add(dataDto.getValue()));

            if (NumberUtils.isExpense(dataDto.getValue())) {
                resultDtoForThisYear.setExpenses(resultDtoForThisYear.getExpenses().add(dataDto.getValue()));
            } else {
                resultDtoForThisYear.setIncomes(resultDtoForThisYear.getIncomes().add(dataDto.getValue()));
            }
            yearResults.put(year, resultDtoForThisYear);
        }
        return yearResults;
    }

    protected Integer getResult(Map<Integer, MiniResultDto> results, boolean reverse) {
        Integer highestMonth = null;
        MiniResultDto highestResultDto = null;
        Map<Integer, MiniResultDto> monthResults = results;
        for (Map.Entry<Integer, MiniResultDto> resultEntry : monthResults.entrySet()) {
            if (highestMonth == null || (!reverse && resultEntry.getValue().getResult().compareTo(highestResultDto.getResult()) > 0) || (reverse && resultEntry.getValue().getResult().compareTo(highestResultDto.getResult()) < 0)) {
                highestMonth = resultEntry.getKey();
                highestResultDto = resultEntry.getValue();
            }
        }
        return highestMonth;
    }


    @Override
    public BigDecimal calculateSavingRatio(List<CategoryDto> profitResults, List<CategoryDto> expensesResults) {
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

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }
}
