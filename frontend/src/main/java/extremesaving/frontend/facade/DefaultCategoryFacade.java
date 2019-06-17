package extremesaving.frontend.facade;

import extremesaving.backend.dao.CategoryDao;
import extremesaving.backend.dao.DataDao;
import extremesaving.backend.model.CategoryModel;
import extremesaving.backend.model.DataModel;
import extremesaving.backend.service.CalculationService;
import extremesaving.backend.dto.CategoryDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component("defaultCategoryFacade")
public class DefaultCategoryFacade implements CategoryFacade {

    @Resource(name = "defaultDataDao")
    private DataDao dataDao;

    @Resource(name = "defaultCategoryDao")
    private CategoryDao categoryDao;

    @Resource(name = "defaultCalculationService")
    private CalculationService calculationService;

    @Override
    public List<CategoryDto> getCategories() {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        List<CategoryModel> categoryModels = categoryDao.findAll();
        for (CategoryModel categoryModel : categoryModels) {
            if (!categoryModel.isTransfer()) {
                categoryDtos.add(convertCategoryModelToCategoryDto(categoryModel));
            }
        }
        Collections.sort(categoryDtos, Comparator.comparing(o -> o.getTotalResults().getResult()));
        Collections.reverse(categoryDtos);
        return categoryDtos;
    }

    private CategoryDto convertCategoryModelToCategoryDto(CategoryModel categoryModel) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryId(categoryModel.getId());
        categoryDto.setCategoryName(categoryModel.getName());

        List<DataModel> categoryResults = dataDao.findBCategory(categoryModel.getId());
        categoryDto.setTotalResults(calculationService.getResults(categoryResults));
        categoryDto.setNonTransferResults(calculationService.getResults(categoryResults.stream().filter(data -> !data.getCategory().isTransfer()).collect(Collectors.toList())));
        return categoryDto;
    }
}
