package extremesaving.calculation.facade;

import extremesaving.calculation.dto.CategoryDto;
import extremesaving.data.model.DataModel;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface CategoryFacade {

    List<CategoryDto> getCategories(Collection<DataModel> dataModels);

    List<CategoryDto> getMostProfitableCategories(Collection<DataModel> dataModels);

    List<CategoryDto> getMostExpensiveCategories(Collection<DataModel> dataModels);

    BigDecimal calculateSavings(String categoryName, BigDecimal percentage, long numberOfDays);
}