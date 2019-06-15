package extremesaving.backend.dao;

import extremesaving.backend.model.DataModel;

import java.util.List;

public interface DataDao {

    List<DataModel> findAll();

    List<DataModel> findByAccount(String accountId);

    List<DataModel> findBCategory(String categoryId);
}
