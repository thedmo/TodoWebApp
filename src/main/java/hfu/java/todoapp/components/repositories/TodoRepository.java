package hfu.java.todoapp.components.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hfu.java.todoapp.common.entities.TodoEntity;

/**
 * Repository interface for Todo entities.
 * Provides database operations for Todo items using Spring Data JPA.
 */
public interface TodoRepository extends JpaRepository<TodoEntity, Integer> {
    /**
     * Deletes a todo item by its ID.
     * @param id The ID of the todo to delete
     */
    void deleteById(Integer id);

    /**
     * Counts the number of todos associated with a specific category.
     * @param categoryId The ID of the category to count todos for
     * @return The number of todos in the specified category
     */
    int countByCategory_Id(Integer categoryId);
}
