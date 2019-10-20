package extremesaving.data.dao;

import extremesaving.data.model.TipOfTheDayModel;

import java.util.List;

public interface TipOfTheDayDao {

    List<TipOfTheDayModel> findAll();
}