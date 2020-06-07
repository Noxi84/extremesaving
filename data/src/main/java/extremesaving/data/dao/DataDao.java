package extremesaving.data.dao;

import java.util.List;

import extremesaving.data.model.DataModel;

public interface DataDao {

    List<DataModel> findAll();
}