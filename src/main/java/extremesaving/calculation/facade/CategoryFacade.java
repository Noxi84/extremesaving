package extremesaving.calculation.facade;

import extremesaving.calculation.dto.CategoryDto;
import extremesaving.data.dto.DataDto;

import java.util.Collection;
import java.util.List;

public interface CategoryFacade {

    List<CategoryDto> getCategories(Collection<DataDto> dataDtos);

    List<CategoryDto> getMostProfitableCategories(Collection<DataDto> dataDtos);

    List<CategoryDto> getMostExpensiveCategories(Collection<DataDto> dataDtos);
}