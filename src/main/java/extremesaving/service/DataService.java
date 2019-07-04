package extremesaving.service;

import extremesaving.dto.CategoryDto;
import extremesaving.model.DataModel;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface DataService {

    List<DataModel> findAll();

    Date getLastItemAdded();

    long getTotalItems();

    BigDecimal getTotalBalance();

    List<CategoryDto> getMostProfitableItems(Collection<DataModel> dataModels);

    List<CategoryDto> getMostExpensiveItems(Collection<DataModel> dataModels);
}