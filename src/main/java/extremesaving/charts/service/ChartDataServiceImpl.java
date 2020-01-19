package extremesaving.charts.service;

import extremesaving.calculation.dto.EstimationResultDto;
import extremesaving.calculation.dto.MiniResultDto;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.calculation.facade.EstimationFacade;
import extremesaving.calculation.util.NumberUtils;
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
    private CalculationFacade calculationFacade;
    private EstimationFacade estimationFacade;

    @Override
    public Map<Date, MiniResultDto> getMonthResults() {
        List<DataDto> dataDtos = dataFacade.findAll();
        ResultDto resultDto = calculationFacade.getResults(dataDtos);
        List<Date> lastMonths = DateUtils.getLastMonths(resultDto.getLastDate());
        List<DataDto> filteredDataDtos = dataDtos.stream()
                .filter(dataDto -> DateUtils.isEqualYearAndMonth(lastMonths, dataDto.getDate()))
                .collect(Collectors.toList());
        Map<Date, MiniResultDto> results = calculationFacade.getMonthResults(filteredDataDtos);
        for (Map.Entry<Date, MiniResultDto> result : results.entrySet()) {
            if (result.getValue().getResult().compareTo(BigDecimal.ZERO) < 0) {
                result.getValue().setResult(BigDecimal.ZERO);
            }
        }
        return results;
    }

    @Override
    public Map<Integer, MiniResultDto> getYearResults() {
        List<DataDto> dataDtos = dataFacade.findAll();
        ResultDto resultDto = calculationFacade.getResults(dataDtos);

        Map<Integer, MiniResultDto> yearResults = calculationFacade.getYearResults(dataDtos);
        for (Map.Entry<Integer, MiniResultDto> result : yearResults.entrySet()) {
            if (result.getValue().getResult().compareTo(BigDecimal.ZERO) < 0) {
                result.getValue().setResult(BigDecimal.ZERO);
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(resultDto.getLastDate());
        addResultDtoIfEmpty(yearResults, calendar.get(Calendar.YEAR) - 11);
        addResultDtoIfEmpty(yearResults, calendar.get(Calendar.YEAR) - 10);
        addResultDtoIfEmpty(yearResults, calendar.get(Calendar.YEAR) - 9);
        addResultDtoIfEmpty(yearResults, calendar.get(Calendar.YEAR) - 8);
        addResultDtoIfEmpty(yearResults, calendar.get(Calendar.YEAR) - 7);
        addResultDtoIfEmpty(yearResults, calendar.get(Calendar.YEAR) - 6);
        addResultDtoIfEmpty(yearResults, calendar.get(Calendar.YEAR) - 5);
        addResultDtoIfEmpty(yearResults, calendar.get(Calendar.YEAR) - 4);
        addResultDtoIfEmpty(yearResults, calendar.get(Calendar.YEAR) - 3);
        addResultDtoIfEmpty(yearResults, calendar.get(Calendar.YEAR) - 2);
        addResultDtoIfEmpty(yearResults, calendar.get(Calendar.YEAR) - 1);
        addResultDtoIfEmpty(yearResults, calendar.get(Calendar.YEAR));

        return yearResults;
    }

    @Override
    public Map<Date, BigDecimal> getGoalLineHistoryResults() {
        Map<Date, BigDecimal> results = new HashMap<>();
        List<DataDto> dataDtos = dataFacade.findAll();

        // Add existing results
        for (DataDto existingDataDto : dataDtos) {
            Set<DataDto> filteredDataDtos = dataDtos.stream().filter(dataDto -> DateUtils.equalDates(dataDto.getDate(), existingDataDto.getDate()) || dataDto.getDate().before(existingDataDto.getDate())).collect(Collectors.toSet());
            ResultDto resultDto = calculationFacade.getResults(filteredDataDtos);
            results.put(existingDataDto.getDate(), resultDto.getResult());
        }
        return results;
    }

    @Override
    public Map<Date, BigDecimal> getGoalLineFutureEstimationResults() {
        List<DataDto> dataDtos = dataFacade.findAll();

        // Add future estimation results with incomes
        ResultDto resultDto = calculationFacade.getResults(dataDtos);
        EstimationResultDto estimationResultDto = estimationFacade.getEstimationResultDto(dataDtos);

        BigDecimal currentValue = resultDto.getResult();
        Calendar cal = Calendar.getInstance();
        cal.setTime(resultDto.getLastDate());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        BigDecimal goal = estimationFacade.getNextGoal(2);

        Map<Date, BigDecimal> predictions = new HashMap<>();
        predictions.put(cal.getTime(), currentValue);

        if (NumberUtils.isIncome(estimationResultDto.getAverageDailyResult())) {
            while (currentValue.compareTo(goal) <= 0) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
                currentValue = currentValue.add(estimationResultDto.getAverageDailyResult());
                if (currentValue.compareTo(BigDecimal.ZERO) > 0) {
                    predictions.put(cal.getTime(), currentValue);
                } else {
                    break;
                }
            }
        }
        return predictions;
    }

    @Override
    public Map<Date, BigDecimal> getGoalLineSurvivalEstimationResults() {
        List<DataDto> dataDtos = dataFacade.findAll();

        // Add future estimation results without incomes
        ResultDto resultDto = calculationFacade.getResults(dataDtos);
        EstimationResultDto estimationResultDto = estimationFacade.getEstimationResultDto(dataDtos);

        BigDecimal currentValue = resultDto.getResult();
        Calendar cal = Calendar.getInstance();
        cal.setTime(resultDto.getLastDate());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Map<Date, BigDecimal> results = new HashMap<>();
        results.put(cal.getTime(), currentValue);

        if (NumberUtils.isExpense(estimationResultDto.getAverageDailyExpense())) {
            while (currentValue.compareTo(BigDecimal.ZERO) > 0) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
                currentValue = currentValue.add(estimationResultDto.getAverageDailyExpense());
                if (currentValue.compareTo(BigDecimal.ZERO) > 0) {
                    results.put(cal.getTime(), currentValue);
                } else {
                    break;
                }
            }
        }
        return results;
    }

    protected void addResultDtoIfEmpty(Map<Integer, MiniResultDto> results, Integer key) {
        if (results.get(key) == null) {
            results.put(key, new MiniResultDto());
        }
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    public void setEstimationFacade(EstimationFacade estimationFacade) {
        this.estimationFacade = estimationFacade;
    }
}