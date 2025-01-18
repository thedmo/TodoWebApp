package hfu.java.todoapp.components.services;

import java.util.List;
import hfu.java.todoapp.common.models.CategoryModel;

/**
 * Interface for managing categories in the application.
 * Provides methods for retrieving, saving, and deleting categories.
 */
public interface CategoryService {
    /**
     * Retrieves all categories.
     * @return A list of CategoryModel objects
     */
    List<CategoryModel> getAll();

    /**
     * Retrieves a category by its unique identifier.
     * @param id The unique identifier of the category
     * @return A CategoryModel object
     */
    CategoryModel getById(int id);

    /**
     * Saves a new category or updates an existing one.
     * @param model The CategoryModel object to save
     * @return The saved CategoryModel object
     */
    CategoryModel save(CategoryModel model);

    /**
     * Retrieves the count of tasks using a specific category.
     * @param id The unique identifier of the category
     * @return The count of tasks using the category
     */
    int countTodos(int id);

    /**
     * Deletes a category by its unique identifier.
     * @param id The unique identifier of the category to delete
     */
    void deleteCategoryById(Integer id);

    /**
     * Deletes all categories.
     */
    void deleteAllEntries();

    /**
     * Retrieves all categories sorted by a specified order.
     * @param typeNumber The type to sort by (0 for name, 1 for color, 2 for usage count)
     * @param ascending Whether to sort in ascending order
     * @return A sorted list of CategoryModel objects
     */
    List<CategoryModel> getAllSorted(int typeNumber, boolean ascending);
} 