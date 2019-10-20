package extremesaving.data.facade;

import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.data.dto.DataDto;
import extremesaving.data.model.DataModel;
import extremesaving.data.model.TipOfTheDayModel;
import extremesaving.data.service.DataService;
import extremesaving.util.NumberUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }
}