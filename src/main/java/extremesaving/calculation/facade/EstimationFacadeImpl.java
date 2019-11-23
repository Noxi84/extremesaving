package extremesaving.calculation.facade;

import extremesaving.calculation.dto.EstimationResultDto;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.service.CalculationService;
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

import static extremesaving.property.PropertyValueEnum.CHART_GOALS_SAVINGS;
import static extremesaving.property.PropertyValueEnum.GOAL_LINE_BAR_CHART_INFLATION_PERCENTAGE;

public class EstimationFacadeImpl implements EstimationFacade {

    private DataFacade dataFacade;
    private CalculationService calculationService;
    private CalculationFacade calculationFacade;

    @Override
    public EstimationResultDto getEstimationResultDto(Collection<DataDto> dataDtos) {
        Map<Date, BigDecimal> combineDays = calculationService.combineDays(dataDtos);
        combineDays = calculationService.filterEstimatedDateRange(combineDays);
        combineDays = calculationService.removeOutliners(combineDays);

        EstimationResultDto estimationResultDto = new EstimationResultDto();
        estimationResultDto.setAverageDailyExpense(calculateAverageDailyExpenseWithFactor(combineDays));
        estimationResultDto.setAverageDailyResult(calculateAverageDailyResult(combineDays));

        return estimationResultDto;
    }

    protected Map<Integer, BigDecimal> getDataMapWithFactor(Map<Date, BigDecimal> dataMap) {
        Date firstDate = dataMap.entrySet().stream().map(entry -> entry.getKey()).min(Date::compareTo).get();
        Date lastDate = dataMap.entrySet().stream().map(entry -> entry.getKey()).max(Date::compareTo).get();

        Map<Integer, BigDecimal> dataWithFactor = new HashMap<>();

        // Init factors so all possible factors are present
        long daysBetween = DateUtils.daysBetween(lastDate, firstDate);
        for (int i = 0; i <= daysBetween; i++) {
            dataWithFactor.put(i, BigDecimal.ZERO);
        }

        // Update factors with their value
        for (Map.Entry<Date, BigDecimal> data : dataMap.entrySet()) {
            Long factor = daysBetween - DateUtils.daysBetween(data.getKey(), lastDate) * -1;
            BigDecimal value = dataWithFactor.get(factor.intValue());
            if (value == null) {
                throw new IllegalStateException("Should not happen because of init factors above.");
            }
            value = value.add(data.getValue());
            dataWithFactor.put(factor.intValue(), value);
        }

        return dataWithFactor;
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

    @Override
    public Long getSurvivalDays() {
        Collection<DataDto> dataDtos = dataFacade.findAll();
        ResultDto resultDto = calculationFacade.getResults(dataDtos);
        EstimationResultDto estimationResultDto = getEstimationResultDto(dataDtos);

        BigDecimal amountLeft = resultDto.getResult();

        BigDecimal inflationPercentage = PropertiesValueHolder.getBigDecimal(GOAL_LINE_BAR_CHART_INFLATION_PERCENTAGE);
        BigDecimal inflation = estimationResultDto.getAverageDailyExpense().multiply(inflationPercentage).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal avgDailyExpenseWithInflation = estimationResultDto.getAverageDailyExpense().add(inflation);

        long dayCounter = 0;
        while (BigDecimal.ZERO.compareTo(amountLeft) <= 0) {
            dayCounter++;
            amountLeft = amountLeft.add(avgDailyExpenseWithInflation);
        }
        return dayCounter - 1;
    }

    @Override
    public BigDecimal getPreviousGoal() {
        String goalsList = PropertiesValueHolder.getString(CHART_GOALS_SAVINGS);
        String[] goals = StringUtils.split(goalsList, ",");
        List<BigDecimal> goalAmounts = new ArrayList<>();
        for (String goal : goals) {
            goalAmounts.add(new BigDecimal(goal));
        }
        BigDecimal nextGoal = getCurrentGoal();
        int nextGoalIndex = goalAmounts.indexOf(nextGoal);
        return goalAmounts.get(nextGoalIndex - 1);
    }

    @Override
    public BigDecimal getCurrentGoal() {
        String[] goals = PropertiesValueHolder.getStringList(CHART_GOALS_SAVINGS);
        List<BigDecimal> goalAmounts = new ArrayList<>();
        for (String goal : goals) {
            goalAmounts.add(new BigDecimal(goal));
        }
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
        String[] goals = PropertiesValueHolder.getStringList(CHART_GOALS_SAVINGS);
        List<BigDecimal> goalAmounts = new ArrayList<>();
        for (String goal : goals) {
            goalAmounts.add(new BigDecimal(goal));
        }
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
        String[] goals = PropertiesValueHolder.getStringList(CHART_GOALS_SAVINGS);
        List<BigDecimal> goalAmounts = new ArrayList<>();
        for (String goal : goals) {
            goalAmounts.add(new BigDecimal(goal));
        }
        BigDecimal nextGoal = getCurrentGoal();
        int nextGoalIndex = goalAmounts.indexOf(nextGoal);
        return goalAmounts.get(nextGoalIndex + index);
    }

    @Override
    public Long getGoalTime(BigDecimal goal) {
        List<DataDto> dataDtos = dataFacade.findAll();
        ResultDto resultDto = calculationFacade.getResults(dataDtos);
        EstimationResultDto estimationResultDto = getEstimationResultDto(dataDtos);

        BigDecimal amount = resultDto.getResult();
        if (goal.compareTo(amount) > 0 || goal.compareTo(amount) == 0) {
            long dayCounter = 0;
            while (goal.compareTo(amount) >= 0) {
                dayCounter++;
                amount = amount.add(estimationResultDto.getAverageDailyResult());
            }
            return dayCounter - 1;
        }
        return 1L;
    }

    @Override
    public Date getGoalReachedDate(BigDecimal goal) {
        List<DataDto> dataDtos = dataFacade.findAll();
        ResultDto resultDto = calculationFacade.getResults(dataDtos);
        BigDecimal amount = resultDto.getResult();
        if (goal.compareTo(amount) < 0) {
            Date lastDate;
            for (int i = dataDtos.size() - 1; i > 0; i--) {
                DataDto dataDto = dataDtos.get(i);
                amount = amount.subtract(dataDto.getValue());
                lastDate = dataDto.getDate();
                if (amount.compareTo(goal) <= 0) {
                    return lastDate;
                }
            }
        }
        return null;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }
}
