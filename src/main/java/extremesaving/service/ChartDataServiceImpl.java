package extremesaving.service;

import extremesaving.dto.AccountDto;
import extremesaving.dto.ResultDto;
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
    public Map<Integer, ResultDto> getMonthlyResults() {
        List<DataModel> dataModels = dataService.findAll().stream()
                .filter(dataModel -> !dataModel.isTransfer())
                .filter(dataModel -> DateUtils.equalYears(dataModel.getDate(), new Date()))
                .collect(Collectors.toList());
        return dataService.getMonthlyResults(dataModels);
    }

    @Override
    public Map<Integer, ResultDto> getYearlyResults() {
        List<DataModel> dataModels = dataService.findAll().stream()
                .filter(dataModel -> !dataModel.isTransfer())
                .filter(dataModel -> DateUtils.equalYears(dataModel.getDate(), new Date()))
                .collect(Collectors.toList());
        Map<Integer, ResultDto> yearlyResults = dataService.getYearlyResults(dataModels);

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

    private void addResultDtoIfEmpty(Map<Integer, ResultDto> results, Integer key) {
        if (results.get(key) == null) {
            results.put(key, new ResultDto());
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
