package extremesaving.data.service;

import extremesaving.data.dao.DataDao;
import extremesaving.data.model.DataModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataServiceImpl implements DataService {

    private DataDao dataDao;

    @Override
    public List<DataModel> findAll() {
        List<DataModel> dataModels = dataDao.findAll();
        Collections.sort(dataModels, Comparator.comparing(DataModel::getDate));
        return dataModels;
    }

    public void setDataDao(DataDao dataDao) {
        this.dataDao = dataDao;
    }
}