package extremesaving.calculation.facade;

import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.service.CalculationService;
import extremesaving.data.dto.DataDto;
import extremesaving.property.PropertiesValueHolder;
import extremesaving.util.NumberUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_DATE_ENABLED;
import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_DATE_RANGE;
import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_OUTLINER_ENABLED;

public class CalculationFacadeImpl implements CalculationFacade {

    private static Map<Integer, ResultDto> calculationCash = new HashMap<>();

    private CalculationService calculationService;

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
    public List<DataDto> removeOutliners(Collection<DataDto> dataDtos) {
        if (Boolean.TRUE.equals(PropertiesValueHolder.getBoolean(CHART_GOALS_ESTIMATION_OUTLINER_ENABLED))) {
            List<DataDto> expenses = calculationService.filterOutliners(dataDtos.stream().filter(dataDto -> NumberUtils.isExpense(dataDto.getValue())).collect(Collectors.toList()));
            List<DataDto> incomes = calculationService.filterOutliners(dataDtos.stream().filter(dataDto -> NumberUtils.isIncome(dataDto.getValue())).collect(Collectors.toList()));
            return dataDtos.stream().filter(dataDto -> expenses.contains(dataDto) || incomes.contains(dataDto)).collect(Collectors.toList());
        }
        return new ArrayList<>(dataDtos);
    }

    @Override
    public List<DataDto> filterEstimatedDateRange(Collection<DataDto> dataDtos) {
        if (Boolean.TRUE.equals(PropertiesValueHolder.getBoolean(CHART_GOALS_ESTIMATION_DATE_ENABLED))) {
            ResultDto resultDto = calculationService.getResultDto(dataDtos);
            long rangeValue = resultDto.getLastDate().getTime() - resultDto.getFirstDate().getTime();
            long pieceValue = rangeValue / 10;
            long estimationRangeValue = PropertiesValueHolder.getLong(CHART_GOALS_ESTIMATION_DATE_RANGE) * pieceValue;
            Date startDate = new Date(resultDto.getLastDate().getTime() - estimationRangeValue);
            return dataDtos.stream().filter(dataDto -> dataDto.getDate().after(startDate)).collect(Collectors.toList());
        }
        return new ArrayList<>(dataDtos);
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }
}
