package extremesaving.data.facade;

import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.data.dto.DataDto;
import extremesaving.data.model.DataModel;
import extremesaving.data.model.TipOfTheDayModel;
import extremesaving.data.service.DataService;
import extremesaving.property.PropertiesValueHolder;
import extremesaving.util.NumberUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_DATE_ENABLED;
import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_DATE_RANGE;
import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_OUTLINER_ENABLED;

public class DataFacadeImpl implements DataFacade {

    private CalculationFacade calculationFacade;
    private DataService dataService;

    @Override
    public List<DataDto> findAll() {
        List<DataModel> dataModels = dataService.findAll();
        return dataModels.stream().map(dataModel -> new DataDto(dataModel)).collect(Collectors.toList());
    }

    @Override
    public String getTipOfTheDay() {
        List<TipOfTheDayModel> tipOfTheDayModels = dataService.findTypeOfTheDays();
        return tipOfTheDayModels.get(NumberUtils.getRandom(0, tipOfTheDayModels.size() - 1)).getText();
    }

    @Override
    public List<DataDto> removeOutliners(Collection<DataDto> dataDtos) {
        if (Boolean.TRUE.equals(PropertiesValueHolder.getBoolean(CHART_GOALS_ESTIMATION_OUTLINER_ENABLED))) {
            List<DataDto> expenses = dataService.filterOutliners(dataDtos.stream().filter(dataDto -> NumberUtils.isExpense(dataDto.getValue())).collect(Collectors.toList()));
            List<DataDto> incomes = dataService.filterOutliners(dataDtos.stream().filter(dataDto -> NumberUtils.isIncome(dataDto.getValue())).collect(Collectors.toList()));
            return dataDtos.stream().filter(dataDto -> expenses.contains(dataDto) || incomes.contains(dataDto)).collect(Collectors.toList());
        }
        return new ArrayList<>(dataDtos);
    }

    @Override
    public List<DataDto> filterEstimatedDateRange(Collection<DataDto> dataDtos) {
        if (Boolean.TRUE.equals(PropertiesValueHolder.getBoolean(CHART_GOALS_ESTIMATION_DATE_ENABLED))) {
            ResultDto resultDto = calculationFacade.getResultDto(dataDtos);
            long rangeValue = resultDto.getLastDate().getTime() - resultDto.getFirstDate().getTime();
            long pieceValue = rangeValue / 10;
            long estimationRangeValue = PropertiesValueHolder.getLong(CHART_GOALS_ESTIMATION_DATE_RANGE) * pieceValue;
            Date startDate = new Date(resultDto.getLastDate().getTime() - estimationRangeValue);
            return dataDtos.stream().filter(dataDto -> dataDto.getDate().after(startDate)).collect(Collectors.toList());
        }
        return new ArrayList<>(dataDtos);
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }
}