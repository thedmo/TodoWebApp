package hfu.java.todoapp.components.services;

import java.util.List;

import org.springframework.stereotype.Service;

import hfu.java.todoapp.common.entities.CategoryEntity;
import hfu.java.todoapp.common.models.CategoryModel;
import hfu.java.todoapp.components.repositories.CategoryRepository;
import hfu.java.todoapp.components.repositories.TodoRepository;
import hfu.java.todoapp.mapper.CategoryMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final TodoRepository todoRepository;
    private List<CategoryModel> categories;

    public CategoryService(CategoryRepository repository, TodoRepository todoRepository) {
        this.repository = repository;
        this.todoRepository = todoRepository;
        refreshCategoryList();
    }
    
    public List<CategoryModel> getAll() {
        refreshCategoryList();
        return categories;
    }

    public CategoryModel getById(int id) {
        refreshCategoryList();
        return categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No entry with id " + id + " found in categories."));
    }

    @Transactional
    public CategoryModel save(CategoryModel model) {
        var catModel = categories.stream().filter(c -> c.getName().equals(model.getName())).findFirst().orElse(model);
        CategoryEntity entity = CategoryMapper.getEntity(catModel);

        CategoryEntity storedEntity = repository.save(entity);
        refreshCategoryList();
        return CategoryMapper.getModel(storedEntity);
    }

    @Transactional
    public int countTodos(int id) {
        return getById(id).getUsageCount();
    }

    @Transactional
    private void refreshCategoryList() {
        List<CategoryEntity> categoryEntities = repository.findAll();
        categories = categoryEntities.stream()
                .map(entity -> {
                    CategoryModel model = CategoryMapper.getModel(entity);
                    model.setUsageCount(todoRepository.countByCategory_Id(entity.getId()));
                    return model;
                })
                .toList();
    }

    @Transactional
    public void deleteCategoryById(Integer id) {
        repository.deleteById(id);
        // refreshCategoryList();
    }

    @Transactional
    public void deleteAllEntries() {
        repository.deleteAll();
        // refreshCategoryList();
    }
}
