package extremesaving.charts.service;

import extremesaving.calculation.dto.AccountDto;
import extremesaving.calculation.dto.MiniResultDto;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.data.model.DataModel;
import extremesaving.calculation.facade.AccountFacade;
import extremesaving.calculation.service.CalculationService;
import extremesaving.data.service.DataService;
import extremesaving.calculation.service.PredictionService;
import extremesaving.util.DateUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ChartDataServiceImpl implements ChartDataService {

    private DataFacade dataFacade;
    private DataService dataService;
    private CalculationService calculationService;
    private AccountFacade accountFacade;
    private PredictionService predictionService;

    @Override
    public Map<String, BigDecimal> getAccountResults() {
        Map<String, BigDecimal> results = new HashMap<>();
        List<AccountDto> accounts = accountFacade.getAccounts().stream()
                .filter(accountDto -> accountDto.getTotalResults().getResult().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());

        for (AccountDto accountDto : accounts) {
            results.put(accountDto.getName(), accountDto.getTotalResults().getResult());
        }
        return results;
    }

    @Override
    public Map<Integer, MiniResultDto> getMonthlyResults() {
        List<DataModel> dataModels = dataService.findAll().stream().filter(dataModel -> DateUtils.equalYears(dataModel.getDate(), new Date())).collect(Collectors.toList());
        Map<Integer, MiniResultDto> results = dataFacade.getMonthlyResults(dataModels);
        for (Map.Entry<Integer, MiniResultDto> result : results.entrySet()) {
            if (result.getValue().getResult().compareTo(BigDecimal.ZERO) < 0) {
                result.getValue().setResult(BigDecimal.ZERO);
            }
        }
        return results;
    }

    @Override
    public Map<Integer, MiniResultDto> getYearlyResults() {
        List<DataModel> dataModels = dataService.findAll();

        Map<Integer, MiniResultDto> yearlyResults = dataFacade.getYearlyResults(dataModels);
        for (Map.Entry<Integer, MiniResultDto> result : yearlyResults.entrySet()) {
            if (result.getValue().getResult().compareTo(BigDecimal.ZERO) < 0) {
                result.getValue().setResult(BigDecimal.ZERO);
            }
        }

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
    public Map<Date, BigDecimal> getGoalLineResults() {
        Map<Date, BigDecimal> predictions = new HashMap<>();

        List<DataModel> dataModels =  dataService.findAll();

        // Add existing results
        for (DataModel existingDataModel : dataModels) {
            Set<DataModel> filteredDataModels = dataModels.stream().filter(dataModel -> DateUtils.equalDates(dataModel.getDate(), existingDataModel.getDate()) || dataModel.getDate().before(existingDataModel.getDate())).collect(Collectors.toSet());
            ResultDto resultDto = calculationService.getResults(filteredDataModels);
            predictions.put(existingDataModel.getDate(), resultDto.getResult());
        }

        // Add future results
        ResultDto resultDto = calculationService.getResults(dataModels);
        List<DataModel> filteredDataModels = calculationService.removeOutliners(dataModels);
        filteredDataModels = calculationService.filterEstimatedDateRange(filteredDataModels);
        ResultDto filteredResultDto = calculationService.getResults(filteredDataModels);
        BigDecimal currentValue = resultDto.getResult();
        Calendar cal = Calendar.getInstance();

        BigDecimal goal = predictionService.getNextGoal(2);
        while (currentValue.compareTo(goal) <= 0) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            currentValue = currentValue.add(filteredResultDto.getAverageDailyResult());
            if (currentValue.compareTo(BigDecimal.ZERO) > 0) {
                predictions.put(cal.getTime(), currentValue);
            } else {
                break;
            }
        }

        return predictions;
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

    public void setAccountFacade(AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

    public void setPredictionService(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }
}