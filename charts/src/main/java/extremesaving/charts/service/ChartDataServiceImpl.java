package extremesaving.charts.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import extremesaving.common.util.DateUtils;
import extremesaving.common.util.NumberUtils;
import extremesaving.data.dto.DataDto;
import extremesaving.data.dto.EstimationResultDto;
import extremesaving.data.dto.MiniResultDto;
import extremesaving.data.dto.ResultDto;
import extremesaving.data.facade.CalculationFacade;
import extremesaving.data.facade.DataFacade;
import extremesaving.data.facade.EstimationFacade;

/**
 * Implementation of ChartDataService.
 * Service for handling the chart-image data sets.
 */
public class ChartDataServiceImpl implements ChartDataService {

    private DataFacade dataFacade;
    private CalculationFacade calculationFacade;
    private EstimationFacade estimationFacade;

    @Override
    public Map<Date, MiniResultDto> getMonthResults() {
        List<DataDto> dataDtos = dataFacade.findAll();
        List<Date> lastMonths = DateUtils.getLastMonths(new Date());
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
    public Map<Date, BigDecimal> getOverallLineHistoryResults() {
        Map<Date, BigDecimal> results = new HashMap<>();
        List<DataDto> dataDtos = dataFacade.findAll();

        // Add existing results
        for (DataDto existingDataDto : dataDtos) {
            Set<DataDto> filteredDataDtos = dataDtos.stream().filter(dataDto -> DateUtils.isEqualDates(dataDto.getDate(), existingDataDto.getDate()) || dataDto.getDate().before(existingDataDto.getDate())).collect(Collectors.toSet());
            ResultDto resultDto = calculationFacade.getResults(filteredDataDtos);
            results.put(existingDataDto.getDate(), resultDto.getResult());
        }
        return results;
    }

    @Override
    public Map<Date, BigDecimal> getOverallLineFutureEstimationResults() {
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

        Map<Date, BigDecimal> predictions = new HashMap<>();
        predictions.put(cal.getTime(), currentValue);

        Calendar maxDate = Calendar.getInstance();
        maxDate.setTime(cal.getTime());
        maxDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.set(Calendar.MONTH, Calendar.JANUARY);
        maxDate.add(Calendar.YEAR, 6);

        if (NumberUtils.isIncome(estimationResultDto.getAverageDailyResult())) {
            while (cal.before(maxDate)) {
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
    public Map<Date, BigDecimal> getOverallLineSurvivalEstimationResults() {
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