package extremesaving.service;

import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.model.TipOfTheDayModel;
import extremesaving.util.NumberUtils;
import extremesaving.util.PropertiesValueHolder;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static extremesaving.util.PropertyValueENum.CHART_GOALS_SAVINGS;
import static extremesaving.util.PropertyValueENum.GOAL_LINE_BAR_CHART_INFLATION_PERCENTAGE;

public class PredictionServiceImpl implements PredictionService {

    private DataService dataService;
    private CalculationService calculationService;

    @Override
    public Long getSurvivalDays() {
        Collection<DataModel> dataModels = dataService.findAll();
        Collection<DataModel> dataModelsWithoutOutliners = calculationService.removeOutliners(dataModels);
        ResultDto resultDto = calculationService.getResults(dataModels);
        ResultDto resultDtoWithoutOutliners = calculationService.getResults(dataModelsWithoutOutliners);

        BigDecimal amountLeft = resultDto.getResult();

        BigDecimal inflationPercentage = new BigDecimal(PropertiesValueHolder.getInstance().getPropValue(GOAL_LINE_BAR_CHART_INFLATION_PERCENTAGE));
        BigDecimal inflation = resultDtoWithoutOutliners.getAverageDailyExpense().multiply(inflationPercentage).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal avgDailyExpenseWithInflation = resultDtoWithoutOutliners.getAverageDailyExpense().add(inflation);

        long dayCounter = 0;
        while (BigDecimal.ZERO.compareTo(amountLeft) <= 0) {
            dayCounter++;
            amountLeft = amountLeft.add(avgDailyExpenseWithInflation);
        }
        return dayCounter - 1;
    }

    @Override
    public BigDecimal getPreviousGoal() {
        String goalsList = PropertiesValueHolder.getInstance().getPropValue(CHART_GOALS_SAVINGS);
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
        String goalsList = PropertiesValueHolder.getInstance().getPropValue(CHART_GOALS_SAVINGS);
        String[] goals = StringUtils.split(goalsList, ",");
        List<BigDecimal> goalAmounts = new ArrayList<>();
        for (String goal : goals) {
            goalAmounts.add(new BigDecimal(goal));
        }

        ResultDto resultDto = calculationService.getResults(dataService.findAll());
        for (BigDecimal goalAmount : goalAmounts) {
            if (resultDto.getResult().compareTo(goalAmount) < 0) {
                return goalAmount;
            }
        }
        return BigDecimal.valueOf(1000000000);
    }

    @Override
    public int getGoalIndex(BigDecimal goalAmount) {
        String goalsList = PropertiesValueHolder.getInstance().getPropValue(CHART_GOALS_SAVINGS);
        String[] goals = StringUtils.split(goalsList, ",");

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
    public BigDecimal getNextGoal() {
        String goalsList = PropertiesValueHolder.getInstance().getPropValue(CHART_GOALS_SAVINGS);
        String[] goals = StringUtils.split(goalsList, ",");
        List<BigDecimal> goalAmounts = new ArrayList<>();
        for (String goal : goals) {
            goalAmounts.add(new BigDecimal(goal));
        }

        BigDecimal nextGoal = getCurrentGoal();
        int nextGoalIndex = goalAmounts.indexOf(nextGoal);
        return goalAmounts.get(nextGoalIndex + 1);
    }

    @Override
    public Long getGoalTime(BigDecimal goal) {
        List<DataModel> dataModels = dataService.findAll();
        ResultDto resultDto = calculationService.getResults(dataModels);
        ResultDto resultDtoWithoutOutliners = calculationService.getResults(calculationService.removeOutliners(dataModels));

        BigDecimal amount = resultDto.getResult();
        if (goal.compareTo(amount) > 0 || goal.compareTo(amount) == 0) {
            long dayCounter = 0;
            while (goal.compareTo(amount) >= 0) {
                dayCounter++;
                amount = amount.add(resultDtoWithoutOutliners.getAverageDailyResult());
            }
            return dayCounter - 1;
        }
        return 1L;
    }

    @Override
    public Date getGoalReachedDate(BigDecimal goal) {
        List<DataModel> dataModels = dataService.findAll();
        ResultDto resultDto = calculationService.getResults(dataModels);

        BigDecimal amount = resultDto.getResult();
        if (goal.compareTo(amount) < 0) {
            Date lastDate = null;
            for (int i = dataModels.size() - 1; i > 0; i--) {
                DataModel dataModel = dataModels.get(i);
                amount = amount.subtract(dataModel.getValue());
                lastDate = dataModel.getDate();
                if (amount.compareTo(goal) <= 0) {
                    return lastDate;
                }
            }
        }
        return null;
    }

    @Override
    public String getTipOfTheDay() {
        List<TipOfTheDayModel> tipOfTheDayModels = dataService.getTipOfTheDays();
        return tipOfTheDayModels.get(NumberUtils.getRandom(0, tipOfTheDayModels.size() - 1)).getText();
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }
}