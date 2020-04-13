package extremesaving.data.service;

import java.util.List;

import extremesaving.data.model.DataModel;

public interface DataService {

    List<DataModel> findAll();
}