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

/**
 * Service class for managing categories in the application.
 * Provides methods for retrieving, saving, and deleting categories.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    /** Repository for category data access */
    private final CategoryRepository repository;

    /** List of categories managed by this service */
    private List<CategoryModel> categories;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
        refreshCategoryList();
    }

    /**
     * Retrieves all categories managed by this service.
     * @return A list of CategoryModel objects
     */
    public List<CategoryModel> getAll() {
        refreshCategoryList();
        return categories;
    }

    /**
     * Retrieves a category by its unique identifier.
     * @param id The unique identifier of the category
     * @return A CategoryModel object
     * @throws EntityNotFoundException if no category is found with the given id
     */
    public CategoryModel getById(int id) {
        refreshCategoryList();
        return categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No entry with id " + id + " found in categories."));
    }

    /**
     * Saves a new category or updates an existing one.
     * @param model The CategoryModel object to save
     * @return The saved CategoryModel object
     */
    @Transactional
    public CategoryModel save(CategoryModel model) {
        var catModel = categories.stream().filter(c -> c.getName().equals(model.getName())).findFirst().orElse(model);
        CategoryEntity entity = CategoryMapper.getEntity(catModel);

        CategoryEntity storedEntity = repository.save(entity);
        refreshCategoryList();
        return CategoryMapper.getModel(storedEntity);
    }

    /**
     * Retrieves the count of tasks using a specific category.
     * @param id The unique identifier of the category
     * @return The count of tasks using the category
     */
    @Transactional
    public int countTodos(int id) {
        return getById(id).getUsageCount();
    }

    /**
     * Refreshes the list of categories managed by this service.
     */
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

    /**
     * Deletes a category by its unique identifier.
     * @param id The unique identifier of the category to delete
     */
    @Transactional
    public void deleteCategoryById(Integer id) {
        repository.deleteById(id);
    }

    /**
     * Deletes all categories managed by this service.
     */
    @Transactional
    public void deleteAllEntries() {
        repository.deleteAll();
    }

    /**
     * Retrieves all categories sorted by a specified order.
     * @param typeNumber The type to sort by (0 for name, 1 for color, 2 for usage count)
     * @param ascending Whether to sort in ascending order
     * @return A sorted list of CategoryModel objects
     */
    public List<CategoryModel> getAllSorted(int typeNumber, boolean ascending) {
        List<CategoryModel> sortedList = new ArrayList<>(getAll());
        
        Comparator<CategoryModel> comparator = switch (typeNumber) {
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
