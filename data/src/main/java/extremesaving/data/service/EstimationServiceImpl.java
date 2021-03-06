package extremesaving.data.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import extremesaving.common.util.DateUtils;
import extremesaving.data.dto.DataDto;
import extremesaving.data.dto.ResultDto;
import extremesaving.common.util.NumberUtils;

public class EstimationServiceImpl implements EstimationService {

    private CalculationService calculationService;

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
    public Map<Date, BigDecimal> removeOutliners(Map<Date, BigDecimal> dataMap) {
        Map<Date, BigDecimal> expenses = filterOutliners(dataMap.entrySet().stream().filter(data -> NumberUtils.isExpense(data.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        Map<Date, BigDecimal> incomes = filterOutliners(dataMap.entrySet().stream().filter(data -> NumberUtils.isIncome(data.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        return Stream.concat(expenses.entrySet().stream(), incomes.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    protected Map<Date, BigDecimal> filterOutliners(Map<Date, BigDecimal> dataMap) {
        List<BigDecimal> sortedValues = dataMap.entrySet().stream().map(entry -> entry.getValue()).sorted(BigDecimal::compareTo).collect(Collectors.toList());
        Double estimationRange = (sortedValues.size() - Double.valueOf(sortedValues.size()) / 1.618) / 2;
        int minIndex = estimationRange.intValue();
        int maxIndex = Math.min(sortedValues.size() - estimationRange.intValue(), sortedValues.size() - 1);
        BigDecimal minValue = sortedValues.get(minIndex);
        BigDecimal maxValue = sortedValues.get(maxIndex);

        Map<Date, BigDecimal> results = new HashMap<>();
        for (Map.Entry<Date, BigDecimal> data : dataMap.entrySet()) {
            if ((minValue.compareTo(data.getValue()) <= 0) && maxValue.compareTo(data.getValue()) >= 0) {
                results.put(data.getKey(), data.getValue());
            }
        }
        return results;
    }

    @Override
    public Map<Date, BigDecimal> filterEstimatedDateRange(Map<Date, BigDecimal> dataMap) {
        ResultDto resultDto = calculationService.getResultDto(dataMap);

        long daysBetween = DateUtils.getDaysBetween(resultDto.getLastDate(), resultDto.getFirstDate());
        Double dblEstimationRange = daysBetween - Double.valueOf(daysBetween) / 1.618;

        Calendar firstDate = Calendar.getInstance();
        firstDate.setTime(resultDto.getLastDate());
        firstDate.add(Calendar.DAY_OF_MONTH, dblEstimationRange.intValue() * -1);

        return dataMap.entrySet().stream().filter(data -> data.getKey().after(firstDate.getTime())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }
}