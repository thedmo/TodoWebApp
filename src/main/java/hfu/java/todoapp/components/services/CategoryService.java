package hfu.java.todoapp.components.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import hfu.java.todoapp.common.entities.CategoryEntity;
import hfu.java.todoapp.common.models.CategoryModel;
import hfu.java.todoapp.components.repositories.CategoryRepository;
import hfu.java.todoapp.mapper.CategoryMapper;
import jakarta.transaction.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private List<CategoryModel> categories;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CategoryModel save(CategoryModel model) {
        CategoryEntity existingCategory = repository.findByName(model.getName());
        
        if (existingCategory != null) {
            // Update existing category
            existingCategory.setColor(model.getColor());
            CategoryEntity updatedEntity = repository.save(existingCategory);
            refreshCategoryList();
            return CategoryMapper.getModel(updatedEntity);
        }
        
        // Create new category
        CategoryEntity storedEntity = repository.save(CategoryMapper.getEntity(model));
        refreshCategoryList();
        return CategoryMapper.getModel(storedEntity);
    }

    public List<CategoryModel> getAll() {
        if (categories == null)
            refreshCategoryList();

        return categories;
    }

    @Transactional
    private void refreshCategoryList() {
        categories = repository.findAll()
                .stream()
                .map(CategoryMapper::getModel)
                .toList();
    }
}
