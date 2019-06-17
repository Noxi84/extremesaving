package extremesaving.frontend.facade;

import extremesaving.backend.dto.CategoryDto;

import java.util.List;

public interface CategoryFacade {

    List<CategoryDto> getCategories();
}
