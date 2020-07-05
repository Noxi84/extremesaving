package extremesaving.data.facade;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import extremesaving.common.util.DateUtils;
import extremesaving.data.dto.DataDto;
import extremesaving.data.dto.MiniResultDto;
import extremesaving.data.dto.ResultDto;
import extremesaving.data.service.CalculationService;
import extremesaving.common.util.NumberUtils;

public class CalculationFacadeImpl implements CalculationFacade {

    private static Map<Integer, ResultDto> calculationCash = new HashMap<>();

    private CalculationService calculationService;
    private CalculationFacade calculationFacade;

    @Override
    public ResultDto getResults(Collection<DataDto> dataDtos) {
        int hashCode = Objects.hash(dataDtos.toArray());
        ResultDto result = calculationCash.get(hashCode);
        if (result == null) {
            result = calculationService.getResultDto(dataDtos);
            calculationCash.put(hashCode, result);
        }
        return result;
    }

    @Override
    public Map<Date, MiniResultDto> getMonthResults(Collection<DataDto> dataDtos) {
        Map<Date, MiniResultDto> monthResults = new HashMap<>();

        ResultDto resultDto = calculationFacade.getResults(dataDtos);
        List<Date> lastMonths = DateUtils.getLastMonths(resultDto.getLastDate());
        List<DataDto> filteredDataDtos = dataDtos.stream()
                .filter(dataDto -> DateUtils.isEqualYearAndMonth(lastMonths, dataDto.getDate()))
                .filter(dataModel -> !dataModel.getCategory().equalsIgnoreCase("..."))
                .collect(Collectors.toList());

        monthResults.put(lastMonths.get(0), new MiniResultDto());
        monthResults.put(lastMonths.get(1), new MiniResultDto());
        monthResults.put(lastMonths.get(2), new MiniResultDto());
        monthResults.put(lastMonths.get(3), new MiniResultDto());
        monthResults.put(lastMonths.get(4), new MiniResultDto());
        monthResults.put(lastMonths.get(5), new MiniResultDto());
        monthResults.put(lastMonths.get(6), new MiniResultDto());
        monthResults.put(lastMonths.get(7), new MiniResultDto());
        monthResults.put(lastMonths.get(8), new MiniResultDto());
        monthResults.put(lastMonths.get(9), new MiniResultDto());
        monthResults.put(lastMonths.get(10), new MiniResultDto());
        monthResults.put(lastMonths.get(11), new MiniResultDto());

        for (DataDto dataDto : filteredDataDtos) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataDto.getDate());

            MiniResultDto resultDtoForThisMonth = null;
            for (Map.Entry<Date, MiniResultDto> monthResult : monthResults.entrySet()) {
                if (DateUtils.isEqualYearAndMonth(monthResult.getKey(), cal.getTime())) {
                    resultDtoForThisMonth = monthResult.getValue();
                    break;
                }
            }
            resultDtoForThisMonth.setResult(resultDtoForThisMonth.getResult().add(dataDto.getValue()));

            if (NumberUtils.isExpense(dataDto.getValue())) {
                resultDtoForThisMonth.setExpenses(resultDtoForThisMonth.getExpenses().add(dataDto.getValue()));
            } else {
                resultDtoForThisMonth.setIncomes(resultDtoForThisMonth.getIncomes().add(dataDto.getValue()));
            }
        }
        return monthResults;
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }
}