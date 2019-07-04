package extremesaving.service;

import extremesaving.dao.DataDao;
import extremesaving.dto.AccountDto;
import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.service.chart.AccountPieChartGenerator;
import extremesaving.service.chart.MonthlyBarChartGenerator;
import extremesaving.service.chart.YearlyBarChartGenerator;
import extremesaving.util.DateUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ChartDataServiceImpl implements ChartDataService {

    private DataDao dataDao;
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
        List<DataModel> dataModels = dataDao.findAll().stream()
                .filter(dataModel -> !dataModel.isTransfer())
                .filter(dataModel -> DateUtils.equalYears(dataModel.getDate(), new Date()))
                .collect(Collectors.toList());

        Map<Integer, ResultDto> monthlyResults = new HashMap<>();
        monthlyResults.put(Calendar.JANUARY, new ResultDto());
        monthlyResults.put(Calendar.FEBRUARY, new ResultDto());
        monthlyResults.put(Calendar.MARCH, new ResultDto());
        monthlyResults.put(Calendar.APRIL, new ResultDto());
        monthlyResults.put(Calendar.MAY, new ResultDto());
        monthlyResults.put(Calendar.JUNE, new ResultDto());
        monthlyResults.put(Calendar.JULY, new ResultDto());
        monthlyResults.put(Calendar.AUGUST, new ResultDto());
        monthlyResults.put(Calendar.SEPTEMBER, new ResultDto());
        monthlyResults.put(Calendar.OCTOBER, new ResultDto());
        monthlyResults.put(Calendar.NOVEMBER, new ResultDto());
        monthlyResults.put(Calendar.DECEMBER, new ResultDto());

        for (DataModel dataModel : dataModels) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataModel.getDate());

            ResultDto resultDtoForThisMonth = monthlyResults.get(cal.get(Calendar.MONTH));
            resultDtoForThisMonth.setResult(resultDtoForThisMonth.getResult().add(dataModel.getValue()));

            if (BigDecimal.ZERO.compareTo(dataModel.getValue()) > 0) {
                resultDtoForThisMonth.setExpenses(resultDtoForThisMonth.getExpenses().add(dataModel.getValue()));
            } else {
                resultDtoForThisMonth.setIncomes(resultDtoForThisMonth.getIncomes().add(dataModel.getValue()));
            }
        }

        return monthlyResults;
    }

    @Override
    public Map<Integer, ResultDto> getYearlyResults() {
        List<DataModel> dataModels = dataDao.findAll().stream()
                .filter(dataModel -> !dataModel.isTransfer())
                .filter(dataModel -> DateUtils.equalYears(dataModel.getDate(), new Date()))
                .collect(Collectors.toList());

        Calendar calendar = Calendar.getInstance();
        Map<Integer, ResultDto> yearlyResults = new HashMap<>();
        yearlyResults.put(calendar.get(Calendar.YEAR) - 11, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 10, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 9, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 8, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 7, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 6, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 5, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 4, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 3, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 2, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR) - 1, new ResultDto());
        yearlyResults.put(calendar.get(Calendar.YEAR), new ResultDto());

        for (DataModel dataModel : dataModels) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataModel.getDate());

            ResultDto resultDtoForThisYear = yearlyResults.get(cal.get(Calendar.YEAR));
            resultDtoForThisYear.setResult(resultDtoForThisYear.getResult().add(dataModel.getValue()));

            if (BigDecimal.ZERO.compareTo(dataModel.getValue()) > 0) {
                resultDtoForThisYear.setExpenses(resultDtoForThisYear.getExpenses().add(dataModel.getValue()));
            } else {
                resultDtoForThisYear.setIncomes(resultDtoForThisYear.getIncomes().add(dataModel.getValue()));
            }
        }
        return yearlyResults;
    }

    public void setDataDao(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
