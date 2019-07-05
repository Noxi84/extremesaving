package extremesaving.dao;

import extremesaving.model.DataModel;

import java.util.List;

public interface DataDao {

    List<DataModel> findAll();
}