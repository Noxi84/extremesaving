package extremesaving.calculation.facade;

import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.service.CalculationService;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.property.PropertiesValueHolder;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static extremesaving.property.PropertyValueEnum.CHART_GOALS_SAVINGS;
import static extremesaving.property.PropertyValueEnum.GOAL_LINE_BAR_CHART_INFLATION_PERCENTAGE;

public class EstimationFacadeImpl implements EstimationFacade {

    private DataFacade dataFacade;
    private CalculationService calculationService;
    private CalculationFacade calculationFacade;

    @Override
    public ResultDto getEstimationResultDto(Collection<DataDto> dataDtos) {
        Collection<DataDto> filteredDataDtos = calculationService.removeOutliners(dataDtos);
        filteredDataDtos = calculationService.filterEstimatedDateRange(filteredDataDtos);
        return calculationFacade.getResults(filteredDataDtos);
    }

    @Override
    public Long getSurvivalDays() {
        Collection<DataDto> dataDtos = dataFacade.findAll();
        ResultDto resultDto = calculationFacade.getResults(dataDtos);
        ResultDto filteredResultDto = getEstimationResultDto(dataDtos);

        BigDecimal amountLeft = resultDto.getResult();

        BigDecimal inflationPercentage = PropertiesValueHolder.getBigDecimal(GOAL_LINE_BAR_CHART_INFLATION_PERCENTAGE);
        BigDecimal inflation = filteredResultDto.getAverageDailyExpense().multiply(inflationPercentage).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal avgDailyExpenseWithInflation = filteredResultDto.getAverageDailyExpense().add(inflation);

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
        ResultDto filteredResultDto = getEstimationResultDto(dataDtos);

        BigDecimal amount = resultDto.getResult();
        if (goal.compareTo(amount) > 0 || goal.compareTo(amount) == 0) {
            long dayCounter = 0;
            while (goal.compareTo(amount) >= 0) {
                dayCounter++;
                amount = amount.add(filteredResultDto.getAverageDailyResult());
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
