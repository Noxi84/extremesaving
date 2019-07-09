package extremesaving.service;

import extremesaving.dto.CategoryDto;
import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.model.TipOfTheDayModel;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

        BigDecimal inflation = resultDto.getAverageDailyExpense().multiply(BigDecimal.valueOf(3)).divide(BigDecimal.valueOf(100));
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
        List<BigDecimal> goalAmounts = Arrays.asList(
                BigDecimal.valueOf(25),
                BigDecimal.valueOf(50),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(250),
                BigDecimal.valueOf(500),
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(2500),
                BigDecimal.valueOf(5000),
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(25000),
                BigDecimal.valueOf(50000),
                BigDecimal.valueOf(100000),
                BigDecimal.valueOf(150000),
                BigDecimal.valueOf(200000),
                BigDecimal.valueOf(250000),
                BigDecimal.valueOf(300000),
                BigDecimal.valueOf(350000),
                BigDecimal.valueOf(400000),
                BigDecimal.valueOf(450000),
                BigDecimal.valueOf(500000),
                BigDecimal.valueOf(100000000));

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