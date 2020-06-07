package extremesaving.data.facade;

import java.util.Collection;
import java.util.List;

import extremesaving.data.dto.CategoryDto;
import extremesaving.data.dto.DataDto;

public interface CategoryFacade {

    List<CategoryDto> getCategories(Collection<DataDto> dataDtos);
}