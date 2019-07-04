package extremesaving.service;

import extremesaving.dto.CategoryDto;
import extremesaving.model.DataModel;

import java.util.Collection;
import java.util.List;

public interface CategoryService {

    List<CategoryDto> getMostProfitableCategories(Collection<DataModel> dataModels);

    List<CategoryDto> getMostExpensiveCategories(Collection<DataModel> dataModels);
}
