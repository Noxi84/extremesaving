package extremesaving.data.service;

import extremesaving.data.dao.DataDao;
import extremesaving.data.dao.TipOfTheDayDao;
import extremesaving.data.model.DataModel;
import extremesaving.data.model.TipOfTheDayModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataServiceImpl implements DataService {

    private DataDao dataDao;
    private TipOfTheDayDao tipOfTheDayDao;

    @Override
    public List<DataModel> findAll() {
        List<DataModel> dataModels = dataDao.findAll();
        Collections.sort(dataModels, Comparator.comparing(DataModel::getDate));
        return dataModels;
    }

    @Override
    public List<TipOfTheDayModel> findTypeOfTheDays() {
        return tipOfTheDayDao.findAll();
    }

    public void setDataDao(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public void setTipOfTheDayDao(TipOfTheDayDao tipOfTheDayDao) {
        this.tipOfTheDayDao = tipOfTheDayDao;
    }
}