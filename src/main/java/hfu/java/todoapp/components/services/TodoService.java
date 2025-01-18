package hfu.java.todoapp.components.services;

import java.util.List;
import hfu.java.todoapp.common.models.TodoModel;

/**
 * Interface for managing Todo items in the application.
 * Handles CRUD operations, sorting, and filtering of todos.
 */
public interface TodoService {
    /**
     * Saves or updates a todo item.
     * If the todo has an ID, it updates the existing entry; otherwise creates a new one.
     * @param model The todo model to save
     * @return The saved todo model with updated information
     */
    TodoModel save(TodoModel model);

    /**
     * Retrieves all todos from the database.
     * @return List of all todo items
     */
    List<TodoModel> getAll();

    /**
     * Retrieves only non-completed todos.
     * @return List of pending todo items
     */
    List<TodoModel> getPending();

    /**
     * Retrieves a specific todo by its ID.
     * @param id The ID of the todo to retrieve
     * @return The found todo model
     */
    TodoModel getById(Integer id);

    /**
     * Deletes a todo item by its ID.
     * @param id The ID of the todo to delete
     */
    void deleteTodoById(Integer id);

    /**
     * Deletes all todo entries from the database.
     */
    void deleteAllEntries();

    /**
     * Updates the completion status of a todo item.
     * @param id The ID of the todo to update
     * @param isDone The new completion status
     */
    void updateTodoStatus(Integer id, boolean isDone);

    /**
     * Returns a sorted list of todos based on specified order.
     * @param typeNumber The type index to sort by (0 for done, 1 for task, 2 for category, 3 for due date, 4 for priority)
     * @param ascending Whether to sort in ascending order
     * @param keepOrder Whether to maintain the last sort order
     * @param isFiltered Whether to only include pending todos
     * @return Sorted list of todos
     */
    List<TodoModel> getSorted(int typeNumber, boolean ascending, boolean keepOrder, boolean isFiltered);
} 