package extremesaving.service;

import extremesaving.dto.AccountDto;
import extremesaving.dto.MiniResultDto;
import extremesaving.model.DataModel;
import extremesaving.util.DateUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ChartDataServiceImpl implements ChartDataService {

    private DataService dataService;
    private CalculationService calculationService;
    private AccountService accountService;

    @Override
    public Map<String, BigDecimal> getAccountResults() {
        Map<String, BigDecimal> results = new HashMap<>();
        List<AccountDto> accounts = accountService.getAccounts().stream()
                .filter(accountDto -> accountDto.getTotalResults().getResult().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());

        for (AccountDto accountDto : accounts) {
            results.put(accountDto.getName(), accountDto.getTotalResults().getResult());
        }
        return results;
    }

    @Override
    public Map<Integer, MiniResultDto> getMonthlyResults() {
        List<DataModel> dataModels = dataService.findAll().stream()
                .filter(dataModel -> DateUtils.equalYears(dataModel.getDate(), new Date()))
                .collect(Collectors.toList());
        return dataService.getMonthlyResults(dataModels);
    }

    @Override
    public Map<Integer, MiniResultDto> getYearlyResults() {
        List<DataModel> dataModels = dataService.findAll();
        Map<Integer, MiniResultDto> yearlyResults = dataService.getYearlyResults(dataModels);

        Calendar calendar = Calendar.getInstance();
        addResultDtoIfEmpty(yearlyResults, calendar.get(Calendar.YEAR) - 11);
        addResultDtoIfEmpty(yearlyResults, calendar.get(Calendar.YEAR) - 10);
        addResultDtoIfEmpty(yearlyResults, calendar.get(Calendar.YEAR) - 9);
        addResultDtoIfEmpty(yearlyResults, calendar.get(Calendar.YEAR) - 8);
        addResultDtoIfEmpty(yearlyResults, calendar.get(Calendar.YEAR) - 7);
        addResultDtoIfEmpty(yearlyResults, calendar.get(Calendar.YEAR) - 6);
        addResultDtoIfEmpty(yearlyResults, calendar.get(Calendar.YEAR) - 5);
        addResultDtoIfEmpty(yearlyResults, calendar.get(Calendar.YEAR) - 4);
        addResultDtoIfEmpty(yearlyResults, calendar.get(Calendar.YEAR) - 3);
        addResultDtoIfEmpty(yearlyResults, calendar.get(Calendar.YEAR) - 2);
        addResultDtoIfEmpty(yearlyResults, calendar.get(Calendar.YEAR) - 1);
        addResultDtoIfEmpty(yearlyResults, calendar.get(Calendar.YEAR));

        return yearlyResults;
    }

    @Override
    public Map<Integer, BigDecimal> getYearPredictions() {
        Map<Integer, BigDecimal> yearPredictions = new HashMap<>();

//        ResultDto resultDto = getResults(dataModels.stream().filter(dataModel -> !dataModel.getCategory().isTransfer()).collect(Collectors.toSet()));
//        ResultDto resultDto = calculationService.getResults(dataModels.stream().collect(Collectors.toSet()));
//        BigDecimal avgDailyIncome = calculationService.calculateAverageDaily(resultDto, CalculationEnum.INCOME);
//        BigDecimal avgDailyExpense = calculationService.calculateAverageDaily(resultDto, CalculationEnum.EXPENSE);

//        for (int yearCounter = 1; yearCounter < 21; yearCounter++) {
//            int year = yearCounter + Calendar.getInstance().get(Calendar.YEAR);
//            Calendar futureYear = Calendar.getInstance();
//            futureYear.add(Calendar.YEAR, yearCounter);
//            long totalDaysUntilFutureYear = DateUtils.daysBetween(futureYear.getTime(), new Date());
//            BigDecimal futureYearResult = avgDailyIncome.multiply(BigDecimal.valueOf(totalDaysUntilFutureYear)).add(avgDailyExpense.multiply(BigDecimal.valueOf((totalDaysUntilFutureYear))));
//            yearPredictions.put(year, resultDto.getResult().add(futureYearResult));
//        }
        return yearPredictions;
    }

    private void addResultDtoIfEmpty(Map<Integer, MiniResultDto> results, Integer key) {
        if (results.get(key) == null) {
            results.put(key, new MiniResultDto());
        }
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}