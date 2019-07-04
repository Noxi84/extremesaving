package extremesaving.service;

import extremesaving.dao.DataDao;
import extremesaving.dto.CategoryDto;
import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class DataServiceImpl implements DataService {

    private DataDao dataDao;
    private CalculationService calculationService;

    @Override
    public List<DataModel> findAll() {
        return dataDao.findAll();
    }

    @Override
    public Date getLastItemAdded() {
        List<DataModel> dataModels = dataDao.findAll();
        ResultDto resultDto = calculationService.getResults(dataModels);
        return resultDto.getLastDate();
    }

    @Override
    public long getTotalItems() {
        List<DataModel> dataModels = dataDao.findAll();
        ResultDto resultDto = calculationService.getResults(dataModels);
        return resultDto.getNumberOfItems();
    }

    @Override
    public BigDecimal getTotalBalance() {
        List<DataModel> dataModels = dataDao.findAll();
        ResultDto resultDto = calculationService.getResults(dataModels);
        return resultDto.getResult();
    }

    @Override
    public List<CategoryDto> getMostProfitableItems(Collection<DataModel> dataModels) {
        return null;
    }

    @Override
    public List<CategoryDto> getMostExpensiveItems(Collection<DataModel> dataModels) {
        return null;
    }

    public void setDataDao(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }
}