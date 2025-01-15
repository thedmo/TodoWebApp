package hfu.java.todoapp.components.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import hfu.java.todoapp.common.entities.CategoryEntity;

/**
 * Repository interface for Category entities.
 * Provides database operations for Categories using Spring Data JPA.
 */
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    /**
     * Finds a category by its name.
     * @param name The name of the category to find
     * @return The found category entity or null if not found
     */
    CategoryEntity findByName(String name);

    /**
     * Finds a category by its ID.
     * @param id The ID of the category to find
     * @return The found category entity or null if not found
     */
    CategoryEntity findById(int id);

    /**
     * Deletes a category by its ID.
     * @param id The ID of the category to delete
     */
    void deleteById(int id);
}
