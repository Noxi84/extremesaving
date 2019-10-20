package extremesaving.data.service;

import extremesaving.data.model.DataModel;
import extremesaving.data.model.TipOfTheDayModel;

import java.util.List;

public interface DataService {

    List<DataModel> findAll();

    List<TipOfTheDayModel> findTypeOfTheDays();
}