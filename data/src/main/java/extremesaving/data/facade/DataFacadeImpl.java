package extremesaving.data.facade;

import java.util.List;
import java.util.stream.Collectors;

import extremesaving.data.dto.DataDto;
import extremesaving.data.model.DataModel;
import extremesaving.data.service.DataService;

public class DataFacadeImpl implements DataFacade {

    private DataService dataService;

    @Override
    public List<DataDto> findAll() {
        List<DataModel> dataModels = dataService.findAll();
        return dataModels.stream().map(dataModel -> new DataDto(dataModel)).collect(Collectors.toList());
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}