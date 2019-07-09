package extremesaving.dao;

import extremesaving.model.TipOfTheDayModel;

import java.util.List;

public interface TipOfTheDayDao {

    List<TipOfTheDayModel> findAll();
}