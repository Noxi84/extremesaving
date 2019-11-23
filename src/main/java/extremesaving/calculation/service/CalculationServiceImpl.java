package extremesaving.calculation.service;

import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.util.NumberUtils;
import extremesaving.data.dto.DataDto;
import extremesaving.property.PropertiesValueHolder;
import extremesaving.util.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_DATE_ENABLED;
import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_OUTLINER_ENABLED;

public class CalculationServiceImpl implements CalculationService {

    @Override
    public Map<Date, BigDecimal> combineDays(Collection<DataDto> dataDtos) {
        Map<Date, BigDecimal> results = new HashMap<>();
        for (DataDto dataDto : dataDtos) {
            BigDecimal value = results.get(dataDto.getDate());
            if (value == null) {
                value = BigDecimal.ZERO;
            }
            value = value.add(dataDto.getValue());
            results.put(dataDto.getDate(), value);
        }
        return results;
    }

    @Override
    public ResultDto getResultDto(Collection<DataDto> dataDtos) {
        ResultDto resultDto = new ResultDto();
        resultDto.setData(new HashSet<>(dataDtos));

        for (DataDto dataDto : dataDtos) {
            enrichtResultDto(resultDto, dataDto.getDate(), dataDto.getValue());
        }
        if (resultDto.getFirstDate() != null) {
            resultDto.setDaysSinceLastUpdate(DateUtils.daysBetween(new Date(), resultDto.getFirstDate()));
        }

        resultDto.setAverageDailyIncome(calculateAverageDailyIncome(resultDto));
        resultDto.setAverageDailyExpense(calculateAverageDailyExpense(resultDto));
        resultDto.setAverageDailyResult(calculateAverageDailyResult(resultDto));

        return resultDto;
    }

    @Override
    public ResultDto getResultDto(Map<Date, BigDecimal> dataMap) {
        ResultDto resultDto = new ResultDto();

        for (Map.Entry<Date, BigDecimal> data : dataMap.entrySet()) {
            Date date = data.getKey();
            BigDecimal value = data.getValue();

            enrichtResultDto(resultDto, date, value);
        }
        if (resultDto.getFirstDate() != null) {
            resultDto.setDaysSinceLastUpdate(DateUtils.daysBetween(new Date(), resultDto.getFirstDate()));
        }

        resultDto.setAverageDailyIncome(calculateAverageDailyIncome(resultDto));
        resultDto.setAverageDailyExpense(calculateAverageDailyExpense(resultDto));
        resultDto.setAverageDailyResult(calculateAverageDailyResult(resultDto));

        return resultDto;
    }

    private void enrichtResultDto(ResultDto resultDto, Date date, BigDecimal value) {
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

    protected BigDecimal calculateAverageDailyIncome(ResultDto resultDto) {
        try {
            if (resultDto.getFirstDate() != null) {
                long daysBetween = DateUtils.daysBetween(new Date(), resultDto.getFirstDate());
                return resultDto.getIncomes().divide(BigDecimal.valueOf(daysBetween), 2, RoundingMode.HALF_DOWN);
            }
            return BigDecimal.ZERO;
        } catch (ArithmeticException ex) {
            return null;
        }
    }

    protected BigDecimal calculateAverageDailyExpense(ResultDto resultDto) {
        try {
            if (resultDto.getFirstDate() != null) {
                long daysBetween = DateUtils.daysBetween(new Date(), resultDto.getFirstDate());
                return resultDto.getExpenses().divide(BigDecimal.valueOf(daysBetween), 2, RoundingMode.HALF_DOWN);
            }
            return BigDecimal.ZERO;
        } catch (ArithmeticException ex) {
            return null;
        }
    }

    protected BigDecimal calculateAverageDailyResult(ResultDto resultDto) {
        try {
            if (resultDto.getFirstDate() != null) {
                long daysBetween = DateUtils.daysBetween(new Date(), resultDto.getFirstDate());
                return resultDto.getResult().divide(BigDecimal.valueOf(daysBetween), 2, RoundingMode.HALF_DOWN);
            }
            return BigDecimal.ZERO;
        } catch (ArithmeticException ex) {
            return null;
        }
    }

    @Override
    public Map<Date, BigDecimal> removeOutliners(Map<Date, BigDecimal> dataMap) {
        if (Boolean.TRUE.equals(PropertiesValueHolder.getBoolean(CHART_GOALS_ESTIMATION_OUTLINER_ENABLED))) {
            Map<Date, BigDecimal> expenses = filterOutliners(dataMap.entrySet().stream().filter(data -> NumberUtils.isExpense(data.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
            Map<Date, BigDecimal> incomes = filterOutliners(dataMap.entrySet().stream().filter(data -> NumberUtils.isIncome(data.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
            return Stream.concat(expenses.entrySet().stream(), incomes.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return dataMap;
    }

    protected Map<Date, BigDecimal> filterOutliners(Map<Date, BigDecimal> dataMap) {
        List<BigDecimal> sortedValues = dataMap.entrySet().stream().map(entry -> entry.getValue()).sorted(BigDecimal::compareTo).collect(Collectors.toList());
        Double estimationRange = (sortedValues.size() - Double.valueOf(sortedValues.size()) / 1.618) / 2;
        int minIndex = estimationRange.intValue();
        int maxIndex = sortedValues.size() - estimationRange.intValue();
        BigDecimal minValue = sortedValues.get(minIndex);
        BigDecimal maxValue = sortedValues.get(maxIndex);

        Map<Date, BigDecimal> results = new HashMap<>();
        for (Map.Entry<Date, BigDecimal> data : dataMap.entrySet()) {
            if ((minValue.compareTo(data.getValue()) <= 0) && maxValue.compareTo(data.getValue()) >= 0) {
                results.put(data.getKey(), data.getValue());
            } else {
//                System.out.println("Removing outliner: " + new SimpleDateFormat("dd/MM/yyyy").format(data.getKey()) + " " + data.getValue() + " (min: " + minValue + ", max: " + maxValue + ")");
            }
        }
        return dataMap;
    }

    @Override
    public Map<Date, BigDecimal> filterEstimatedDateRange(Map<Date, BigDecimal> dataMap) {
        if (Boolean.TRUE.equals(PropertiesValueHolder.getBoolean(CHART_GOALS_ESTIMATION_DATE_ENABLED))) {
            ResultDto resultDto = getResultDto(dataMap);

            long daysBetween = DateUtils.daysBetween(resultDto.getLastDate(), resultDto.getFirstDate());
            Double dblEstimationRange = daysBetween - Double.valueOf(daysBetween) / 1.618;

            Calendar firstDate = Calendar.getInstance();
            firstDate.setTime(resultDto.getLastDate());
            firstDate.add(Calendar.DAY_OF_MONTH, dblEstimationRange.intValue() * -1);

            return dataMap.entrySet().stream().filter(data -> data.getKey().after(firstDate.getTime())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return dataMap;
    }
}