package extremesaving.calculation.facade;

import java.util.Collection;
import java.util.List;

import extremesaving.calculation.dto.CategoryDto;
import extremesaving.data.dto.DataDto;

public interface CategoryFacade {

    List<CategoryDto> getCategories(Collection<DataDto> dataDtos);
}