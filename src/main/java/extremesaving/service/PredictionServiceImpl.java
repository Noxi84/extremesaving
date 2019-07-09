package extremesaving.service;

import extremesaving.dto.CategoryDto;
import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.model.TipOfTheDayModel;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;
import extremesaving.util.PropertiesValueHolder;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static extremesaving.util.PropertyValueENum.GOAL_LINE_BAR_CHART_INFLATION_PERCENTAGE;
import static extremesaving.util.PropertyValueENum.HISTORY_LINE_CHART_GOALS;

public class PredictionServiceImpl implements PredictionService {

    private DataService dataService;
    private CalculationService calculationService;
    private CategoryService categoryService;

    @Override
    public CategoryDto getRandomExpensiveCategory() {
        List<CategoryDto> categoryDtos = categoryService.getCategories(dataService.findAll());
        Calendar lastYear = Calendar.getInstance();
        List<CategoryDto> expensiveCategoryDtos = categoryDtos.stream()
                .filter(categoryDto -> categoryDto.getTotalResults().getResult().compareTo(BigDecimal.ZERO) < 0)
                .filter(categoryDto -> DateUtils.equalYears(categoryDto.getTotalResults().getLastDate(), new Date()) || DateUtils.equalYears(categoryDto.getTotalResults().getLastDate(), lastYear.getTime()))
                .sorted(Comparator.comparing(o -> o.getTotalResults().getLastDate()))
                .collect(Collectors.toList());
        return expensiveCategoryDtos.get(NumberUtils.getRandom(0, Math.min(expensiveCategoryDtos.size() - 1, 3)));
    }

    @Override
    public CategoryDto getRandomProfitCategory() {
        List<CategoryDto> categoryDtos = categoryService.getCategories(dataService.findAll());
        Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);
        List<CategoryDto> profitableCategoryDtos = categoryDtos.stream()
                .filter(categoryDto -> categoryDto.getTotalResults().getResult().compareTo(BigDecimal.ZERO) > 0)
                .filter(categoryDto -> DateUtils.equalYears(categoryDto.getTotalResults().getLastDate(), new Date()) || DateUtils.equalYears(categoryDto.getTotalResults().getLastDate(), lastYear.getTime()))
                .sorted(Comparator.comparing(o -> o.getTotalResults().getLastDate()))
                .collect(Collectors.toList());
        return profitableCategoryDtos.get(NumberUtils.getRandom(0, Math.min(profitableCategoryDtos.size() - 1, 3)));
    }

    @Override
    public Long getSurvivalDays() {
        List<DataModel> dataModels = dataService.findAll();
        ResultDto resultDto = calculationService.getResults(dataModels);

        BigDecimal amountLeft = resultDto.getResult();

        BigDecimal inflationPercentage = new BigDecimal(PropertiesValueHolder.getInstance().getPropValue(GOAL_LINE_BAR_CHART_INFLATION_PERCENTAGE));
        BigDecimal inflation = resultDto.getAverageDailyExpense().multiply(inflationPercentage).divide(BigDecimal.valueOf(100));
        BigDecimal avgDailyExpenseWithInflation = resultDto.getAverageDailyExpense().add(inflation);
        long dayCounter = 0;
        while (BigDecimal.ZERO.compareTo(amountLeft) <= 0) {
            dayCounter++;
            amountLeft = amountLeft.add(avgDailyExpenseWithInflation);
        }
        return dayCounter - 1;
    }

    @Override
    public BigDecimal getNextGoal() {
        String goalsList = PropertiesValueHolder.getInstance().getPropValue(HISTORY_LINE_CHART_GOALS);
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
    public Long getGoalTime(BigDecimal goal) {
        List<DataModel> dataModels = dataService.findAll();
        ResultDto resultDto = calculationService.getResults(dataModels);

        BigDecimal amount = resultDto.getResult();

        long dayCounter = 0;
        while (goal.compareTo(amount) >= 0) {
            dayCounter++;
            amount = amount.add(resultDto.getAverageDailyResult());
        }
        return dayCounter - 1;
    }

    @Override
    public BigDecimal getPredictionAmount(Date endDate) {
        List<DataModel> dataModels = dataService.findAll();
        long numberOfDays = DateUtils.daysBetween(endDate, new Date());

        ResultDto resultDto = calculationService.getResults(dataModels);
        BigDecimal amount = resultDto.getResult();
        Calendar cal = Calendar.getInstance();
        for (long i = 0; i < numberOfDays; i++) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            amount = amount.add(resultDto.getAverageDailyResult());
        }
        return amount;
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

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}