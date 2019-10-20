package extremesaving.charts.service;

import extremesaving.calculation.dto.MiniResultDto;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.service.CalculationService;
import extremesaving.calculation.service.PredictionService;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.util.DateUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChartDataServiceImpl implements ChartDataService {

    private DataFacade dataFacade;
    private CalculationService calculationService;
    private PredictionService predictionService;

    @Override
    public Map<Integer, MiniResultDto> getMonthlyResults() {
        List<DataDto> dataDtos = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(dataDto.getDate(), new Date())).collect(Collectors.toList());
        Map<Integer, MiniResultDto> results = dataFacade.getMonthlyResults(dataDtos);
        for (Map.Entry<Integer, MiniResultDto> result : results.entrySet()) {
            if (result.getValue().getResult().compareTo(BigDecimal.ZERO) < 0) {
                result.getValue().setResult(BigDecimal.ZERO);
            }
        }
        return results;
    }

    @Override
    public Map<Integer, MiniResultDto> getYearlyResults() {
        List<DataDto> dataDtos = dataFacade.findAll();

        Map<Integer, MiniResultDto> yearlyResults = dataFacade.getYearlyResults(dataDtos);
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

        List<DataDto> dataDtos = dataFacade.findAll();

        // Add existing results
        for (DataDto existingDataDto : dataDtos) {
            Set<DataDto> filteredDataDtos = dataDtos.stream().filter(dataDto -> DateUtils.equalDates(dataDto.getDate(), existingDataDto.getDate()) || dataDto.getDate().before(existingDataDto.getDate())).collect(Collectors.toSet());
            ResultDto resultDto = calculationService.getResults(filteredDataDtos);
            predictions.put(existingDataDto.getDate(), resultDto.getResult());
        }

        // Add future results
        ResultDto resultDto = calculationService.getResults(dataDtos);
        List<DataDto> filteredDataDtos = calculationService.removeOutliners(dataDtos);
        filteredDataDtos = calculationService.filterEstimatedDateRange(filteredDataDtos);
        ResultDto filteredResultDto = calculationService.getResults(filteredDataDtos);
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

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    public void setPredictionService(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }
}