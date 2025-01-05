package hfu.java.todoapp.components.services;

import java.util.List;

import org.springframework.stereotype.Service;

import hfu.java.todoapp.common.entities.CategoryEntity;
import hfu.java.todoapp.common.models.CategoryModel;
import hfu.java.todoapp.components.repositories.CategoryRepository;
import hfu.java.todoapp.mapper.CategoryMapper;
import jakarta.persistence.EntityNotFoundException;
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
        if (categories == null)
            refreshCategoryList();

        var catModel = categories.stream().filter(c -> c.getName().equals(model.getName())).findFirst().orElse(model);
        CategoryEntity entity = CategoryMapper.getEntity(catModel);

        CategoryEntity storedEntity = repository.save(entity);
        refreshCategoryList();
        return CategoryMapper.getModel(storedEntity);
    }

    public List<CategoryModel> getAll() {
        if (categories == null)
            refreshCategoryList();

        return categories;
    }

    public CategoryModel getById(int id) {
        return categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No entry with id " + id + " found in categories."));
    }

    @Transactional
    private void refreshCategoryList() {
        categories = repository.findAll()
                .stream()
                .map(CategoryMapper::getModel)
                .toList();
    }

    @Transactional
    public void deleteCategoryById(Integer id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteAllEntries() {
        repository.deleteAll();
    }
}
