package extremesaving.data.facade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import extremesaving.common.util.DateUtils;
import extremesaving.data.dto.DataDto;
import extremesaving.data.dto.EstimationResultDto;
import extremesaving.data.service.EstimationService;
import extremesaving.common.util.NumberUtils;

public class EstimationFacadeImpl implements EstimationFacade {

    private EstimationService estimationService;

    @Override
    public EstimationResultDto getEstimationResultDto(Collection<DataDto> dataDtos) {
        Map<Date, BigDecimal> estimationDataDtos = estimationService.combineDays(dataDtos);
        estimationDataDtos = estimationService.removeOutliners(estimationDataDtos);
        estimationDataDtos = estimationService.filterEstimatedDateRange(estimationDataDtos);

        EstimationResultDto estimationResultDto = new EstimationResultDto();
        estimationResultDto.setAverageDailyExpense(calculateAverageDailyExpenseWithFactor(estimationDataDtos));
        estimationResultDto.setAverageDailyResult(calculateAverageDailyResult(estimationDataDtos));

        return estimationResultDto;
    }

    protected BigDecimal calculateAverageDailyExpenseWithFactor(Map<Date, BigDecimal> dataMap) {
        Map<Integer, BigDecimal> dataMapWithfactor = getDataMapWithFactor(dataMap);

        long totalNumberOfDays = 0;
        BigDecimal totalExpenses = BigDecimal.ZERO;

        for (Map.Entry<Integer, BigDecimal> data : dataMapWithfactor.entrySet()) {
            if (NumberUtils.isExpense(data.getValue())) {
                totalNumberOfDays += data.getKey();
                totalExpenses = totalExpenses.add(data.getValue().multiply(BigDecimal.valueOf(data.getKey())));
            }
        }

        try {
            return totalExpenses.divide(BigDecimal.valueOf(totalNumberOfDays), 2, RoundingMode.HALF_DOWN);
        } catch (ArithmeticException ex) {
            return null;
        }
    }

    protected BigDecimal calculateAverageDailyResult(Map<Date, BigDecimal> dataMap) {
        Map<Integer, BigDecimal> dataMapWithfactor = getDataMapWithFactor(dataMap);

        long totalNumberOfDays = 0;
        BigDecimal totalResult = BigDecimal.ZERO;

        for (Map.Entry<Integer, BigDecimal> data : dataMapWithfactor.entrySet()) {
            if (NumberUtils.isExpense(data.getValue()) || NumberUtils.isIncome(data.getValue())) {
                totalNumberOfDays += data.getKey();
                totalResult = totalResult.add(data.getValue().multiply(BigDecimal.valueOf(data.getKey())));
            }
        }

        try {
            return totalResult.divide(BigDecimal.valueOf(totalNumberOfDays), 2, RoundingMode.HALF_DOWN);
        } catch (ArithmeticException ex) {
            return null;
        }
    }

    protected Map<Integer, BigDecimal> getDataMapWithFactor(Map<Date, BigDecimal> dataMap) {
        Date firstDate = dataMap.entrySet().stream().map(entry -> entry.getKey()).min(Date::compareTo).get();
        Date lastDate = dataMap.entrySet().stream().map(entry -> entry.getKey()).max(Date::compareTo).get();

        Map<Integer, BigDecimal> dataWithFactor = new HashMap<>();

        // Init factors so all possible factors are present
        long daysBetween = DateUtils.getDaysBetween(lastDate, firstDate);
        for (int i = 0; i <= daysBetween; i++) {
            dataWithFactor.put(i, BigDecimal.ZERO);
        }

        // Update factors with their value
        for (Map.Entry<Date, BigDecimal> data : dataMap.entrySet()) {
            Long factor = daysBetween - DateUtils.getDaysBetween(data.getKey(), lastDate) * -1;
            BigDecimal value = dataWithFactor.get(factor.intValue());
            if (value == null) {
                throw new IllegalStateException("Should not happen because of init factors above.");
            }
            value = value.add(data.getValue());
            dataWithFactor.put(factor.intValue(), value);
        }

        return dataWithFactor;
    }

    public void setEstimationService(EstimationService estimationService) {
        this.estimationService = estimationService;
    }
}
