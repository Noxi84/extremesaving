package extremesaving.data.facade;

import extremesaving.data.dto.DataDto;
import extremesaving.data.model.DataModel;
import extremesaving.data.model.TipOfTheDayModel;
import extremesaving.data.service.DataService;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DataFacadeImpl implements DataFacade {

    private DataService dataService;

    @Override
    public List<DataDto> findAll() {
        List<DataModel> dataModels = dataService.findAll();
        return dataModels.stream().map(dataModel -> new DataDto(dataModel)).collect(Collectors.toList());
    }

    @Override
    public String getTipOfTheDay() {
        List<TipOfTheDayModel> tipOfTheDayModels = dataService.findTypeOfTheDays();
        return tipOfTheDayModels.get(getRandom(0, tipOfTheDayModels.size() - 1)).getText();
    }

    protected int getRandom(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}