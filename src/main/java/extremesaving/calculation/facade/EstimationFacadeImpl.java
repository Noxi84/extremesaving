package extremesaving.calculation.facade;

import extremesaving.calculation.dto.EstimationResultDto;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.service.EstimationService;
import extremesaving.calculation.util.NumberUtils;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.property.PropertiesValueHolder;
import extremesaving.util.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static extremesaving.property.PropertyValueEnum.GOALS;

public class EstimationFacadeImpl implements EstimationFacade {

    private DataFacade dataFacade;
    private CalculationFacade calculationFacade;
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

    @Override
    public BigDecimal getPreviousGoal() {
        List<BigDecimal> goalAmounts = getGoalAmounts();
        BigDecimal nextGoal = getCurrentGoal();
        int nextGoalIndex = goalAmounts.indexOf(nextGoal);
        return goalAmounts.get(Math.max(0, nextGoalIndex - 1));
    }

    @Override
    public BigDecimal getCurrentGoal() {
        List<BigDecimal> goalAmounts = getGoalAmounts();
        ResultDto resultDto = calculationFacade.getResults(dataFacade.findAll());
        for (BigDecimal goalAmount : goalAmounts) {
            if (resultDto.getResult().compareTo(goalAmount) < 0) {
                return goalAmount;
            }
        }
        return BigDecimal.valueOf(1000000000);
    }

    @Override
    public int getGoalIndex(BigDecimal goalAmount) {
        List<BigDecimal> goalAmounts = getGoalAmounts();
        for (int i = 0; i < goalAmounts.size(); i++) {
            BigDecimal goal = goalAmounts.get(i);
            if (goalAmount.compareTo(goal) < 0 || goalAmount.compareTo(goal) == 0) {
                return i + 1;
            }
        }
        return 18;
    }

    @Override
    public BigDecimal getNextGoal(int index) {
        List<BigDecimal> goalAmounts = getGoalAmounts();
        BigDecimal nextGoal = getCurrentGoal();
        int nextGoalIndex = goalAmounts.indexOf(nextGoal);
        return goalAmounts.get(Math.min(nextGoalIndex + index, goalAmounts.size() - 1));
    }

    protected List<BigDecimal> getGoalAmounts() {
        String[] goals = PropertiesValueHolder.getStringList(GOALS);
        List<BigDecimal> goalAmounts = new ArrayList<>();
        for (String goal : goals) {
            goalAmounts.add(new BigDecimal(StringUtils.trim(goal)));
        }
        return goalAmounts;
    }

    @Override
    public BigDecimal calculateGoalRatio() {
        List<BigDecimal> goalAmounts = getGoalAmounts();
        BigDecimal goalAmountsTotal = goalAmounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal averageGoal = goalAmountsTotal.divide(BigDecimal.valueOf(goalAmounts.size()), RoundingMode.HALF_UP);
        BigDecimal totalResult = calculationFacade.getResults(dataFacade.findAll()).getResult();

        if (totalResult.compareTo(averageGoal) > 0) {
            return BigDecimal.valueOf(100);
        }
        return totalResult.divide(averageGoal, 2, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100));
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }

    public void setEstimationService(EstimationService estimationService) {
        this.estimationService = estimationService;
    }
}
