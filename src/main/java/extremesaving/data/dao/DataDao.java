package extremesaving.data.dao;

import extremesaving.data.model.DataModel;

import java.util.List;

public interface DataDao {

    List<DataModel> findAll();
}