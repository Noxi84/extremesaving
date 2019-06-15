package dao;

import model.CategoryModel;

import java.util.List;
import java.util.Optional;

public interface CategoryDao {

    List<CategoryModel> findAll();

    Optional<CategoryModel> getCategoryById(String id);

    boolean saveOrUpdateCategory(String id, String name);

    boolean removeCategory(String id);
}