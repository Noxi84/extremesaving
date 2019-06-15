package dao;

import model.DataModel;

import java.util.List;

public interface DataDao {

    List<DataModel> findAll();

    List<DataModel> findByAccount(String accountId);

    List<DataModel> findBCategory(String categoryId);
}
