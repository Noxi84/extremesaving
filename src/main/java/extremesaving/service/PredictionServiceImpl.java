package extremesaving.service;

import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.util.DateUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PredictionServiceImpl implements PredictionService {

    private DataService dataService;
    private CalculationService calculationService;
    private CategoryService categoryService;

    @Override
    public Long getSurvivalDays() {
        List<DataModel> dataModels = dataService.findAll();
        ResultDto resultDto = calculationService.getResults(dataModels);
        ResultDto nonTransferResultDto = calculationService.getResults(dataModels.stream().filter(dataModel -> !dataModel.isTransfer()).collect(Collectors.toList()));

        BigDecimal amountLeft = resultDto.getResult();

        BigDecimal inflation = nonTransferResultDto.getAverageDailyExpense().multiply(BigDecimal.valueOf(3)).divide(BigDecimal.valueOf(100));
        BigDecimal avgDailyExpenseWithInflation = nonTransferResultDto.getAverageDailyExpense().add(inflation);
        long dayCounter = 0;
        while (BigDecimal.ZERO.compareTo(amountLeft) <= 0) {
            dayCounter++;
            amountLeft = amountLeft.add(avgDailyExpenseWithInflation);
        }
        return dayCounter - 1;
    }

    @Override
    public BigDecimal getPredictionAmount(Date endDate) {
        List<DataModel> dataModels = dataService.findAll();
        long numberOfDays = DateUtils.daysBetween(endDate, new Date());

        ResultDto resultDto = calculationService.getResults(dataModels);
        ResultDto nonTransferResultDto = calculationService.getResults(dataModels.stream().filter(dataModel -> !dataModel.isTransfer()).collect(Collectors.toList()));
        BigDecimal amount = resultDto.getResult();
//        BigDecimal inflation = nonTransferResultDto.getAverageDailyResult().multiply(BigDecimal.valueOf(3)).divide(BigDecimal.valueOf(100));
        Calendar cal = Calendar.getInstance();
        for (long i = 0; i < numberOfDays; i++) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            amount = amount.add(nonTransferResultDto.getAverageDailyResult());
        }
        return amount;
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