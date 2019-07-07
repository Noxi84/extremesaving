package extremesaving.service;

import extremesaving.dto.CategoryDto;
import extremesaving.model.DataModel;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface CategoryService {

    List<CategoryDto> getCategories(Collection<DataModel> dataModels);

    List<CategoryDto> getMostProfitableCategories(Collection<DataModel> dataModels);

    List<CategoryDto> getMostExpensiveCategories(Collection<DataModel> dataModels);

    BigDecimal calculateSavings(String categoryName, BigDecimal percentage, long numberOfDays);
}
