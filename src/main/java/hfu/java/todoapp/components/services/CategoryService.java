package hfu.java.todoapp.components.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

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

    public List<CategoryModel> getAllSorted(int column, boolean ascending) {
        List<CategoryModel> sortedList = new ArrayList<>(getAll());
        
        Comparator<CategoryModel> comparator = switch (column) {
            case 0 -> Comparator.comparing(CategoryModel::getName);
            case 1 -> Comparator.comparing(CategoryModel::getColor);
            case 2 -> Comparator.comparing(CategoryModel::getUsageCount);
            default -> Comparator.comparing(CategoryModel::getName);
        };
        
        if (!ascending) {
            comparator = comparator.reversed();
        }
        
        sortedList.sort(comparator);
        return sortedList;
    }
}
