package dao;

import model.CategoryModel;
import util.csvparser.CategoryCsvParser;
import util.csvparser.data.CategoryCsv;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultCategoryDao implements CategoryDao {

    private CategoryCsvParser categoryCsvParser = new CategoryCsvParser();

    @Override
    public List<CategoryModel> findAll() {
        List<CategoryCsv> csvList = categoryCsvParser.parseCsv();
        List<CategoryModel> categoryModels = new ArrayList<>();
        for (CategoryCsv csv : csvList) {
            categoryModels.add(convert(csv));
        }
        return categoryModels;
    }

    @Override
    public Optional<CategoryModel> getCategoryById(String id) {
        List<CategoryModel> models = findAll();
        return models.stream().filter(model -> model.getId().equals(id)).findFirst();
    }

    private CategoryModel convert(CategoryCsv categoryCsv) {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(categoryCsv.getId());
        categoryModel.setName(categoryCsv.getName());
        categoryModel.setTransfer("1".equals(categoryCsv.getTransfer()));
        return categoryModel;
    }

    @Override
    public boolean saveOrUpdateCategory(String id, String name) {
        return false;
    }

    @Override
    public boolean removeCategory(String id) {
        return false;
    }
}
