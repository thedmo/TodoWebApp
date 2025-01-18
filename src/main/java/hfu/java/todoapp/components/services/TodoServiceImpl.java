package hfu.java.todoapp.components.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import hfu.java.todoapp.common.entities.*;
import hfu.java.todoapp.common.models.TodoModel;
import hfu.java.todoapp.components.repositories.CategoryRepository;
import hfu.java.todoapp.components.repositories.TodoRepository;
import hfu.java.todoapp.mapper.CategoryMapper;
import hfu.java.todoapp.mapper.TodoMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

/**
 * Service class for managing Todo items in the application.
 * Handles CRUD operations, sorting, and filtering of todos.
 */
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;
    private List<TodoModel> todos;

    /**
     * Constructs a new TodoService with required repositories.
     * Initializes the todos cache on creation.
     */
    public TodoServiceImpl(TodoRepository todoRepository, CategoryRepository categoryRepository) {
        this.todoRepository = todoRepository;
        this.categoryRepository = categoryRepository;
        refreshTodos();
    }

    /**
     * Saves or updates a todo item.
     * If the todo has an ID, it updates the existing entry; otherwise creates a new one.
     * @param model The todo model to save
     * @return The saved todo model with updated information
     */
    @Transactional
    public TodoModel save(TodoModel model) {
        // Find or create the category
        CategoryEntity categoryEntity = categoryRepository.findByName(model.getCategory().getName());

        // Map the todo
        TodoEntity todoEntity = TodoMapper.map(model);
        todoEntity.setCategory(categoryEntity);

        // Save the todo
        TodoEntity savedEntity = todoRepository.save(todoEntity);

        // Return the saved model
        TodoModel savedModel = TodoMapper.getModel(savedEntity);
        savedModel.setCategory(CategoryMapper.getModel(savedEntity.getCategory()));

        refreshTodos();
        return savedModel;
    }

    /**
     * Retrieves all todos from the database.
     * @return List of all todo items
     */
    public List<TodoModel> getAll() {
        refreshTodos();
        return todos;
    }

    /**
     * Retrieves only non-completed todos.
     * @return List of pending todo items
     */
    public List<TodoModel> getPending() {
        refreshTodos();
        return getAll()
                .stream()
                .filter(todo -> !todo.isDone())
                .toList();
    }

    /**
     * Retrieves a specific todo by its ID.
     * @param id The ID of the todo to retrieve
     * @return The found todo model
     * @throws EntityNotFoundException if todo with given ID doesn't exist
     */
    public TodoModel getById(Integer id) {
        refreshTodos();
        return todos.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));
    }

    /**
     * Refreshes the internal todo cache from the database.
     * Maps entities to models and sets up category relationships.
     */
    @Transactional
    private void refreshTodos() {
        todos = todoRepository.findAll()
                .stream()
                .map(e -> {
                    TodoModel m = TodoMapper.getModel(e);
                    m.setCategory(CategoryMapper.getModel(e.getCategory()));
                    return m;
                })
                .toList();
    }

    /**
     * Deletes a todo item by its ID.
     * @param id The ID of the todo to delete
     */
    @Transactional
    public void deleteTodoById(Integer id) {
        todoRepository.deleteById(id);
    }

    /**
     * Deletes all todo entries from the database.
     * Primarily used for testing purposes.
     */
    @Transactional
    public void deleteAllEntries() {
        todoRepository.deleteAll();
    }

    /**
     * Updates the completion status of a todo item.
     * @param id The ID of the todo to update
     * @param isDone The new completion status
     * @throws EntityNotFoundException if todo with given ID doesn't exist
     */
    @Transactional
    public void updateTodoStatus(Integer id, boolean isDone) {
        // Find the todo model from the local list
        TodoModel todoModel = getAll().stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));

        // Update the status
        todoModel.setDone(isDone);

        // Save the updated model using the save method
        save(todoModel);
    }

    private List<Integer> lastSortOrder;

    /**
     * Returns a sorted list of todos based on specified order.
     * @param typeNumber The type index to sort by (0 for done, 1 for task, 2 for category, 3 for due date, 4 for priority)
     * @param ascending Whether to sort in ascending order
     * @param keepOrder Whether to maintain the last sort order
     * @param isFiltered Whether to only include pending todos
     * @return Sorted list of todos
     */
    public List<TodoModel> getSorted(int typeNumber, boolean ascending, boolean keepOrder, boolean isFiltered) {
        List<TodoModel> resultList = new ArrayList<>(isFiltered ? getPending() : getAll());

        if (keepOrder) {
            if (lastSortOrder != null) {
                Map<Integer, Integer> orderMap = new HashMap<>();
                for (int i = 0; i < lastSortOrder.size(); i++) {
                    orderMap.put(lastSortOrder.get(i), i);
                }
                resultList.sort(Comparator.comparing(todo -> orderMap.getOrDefault(todo.getId(), Integer.MAX_VALUE)));
            }
        } else {

            Comparator<TodoModel> comparator = switch (typeNumber) {
                case 0 -> Comparator.comparing(TodoModel::isDone);
                case 1 -> Comparator.comparing(TodoModel::getTask);
                case 2 -> Comparator.comparing(todo -> todo.getCategory().getName());
                case 3 -> Comparator.comparing(TodoModel::getDueDate,
                        Comparator.nullsLast(Comparator.naturalOrder()));
                case 4 -> Comparator.comparing(todo -> todo.getPriority().getValue());
                default -> Comparator.comparing(TodoModel::isDone);
            };

            if (!ascending) {
                comparator = comparator.reversed();
            }

            resultList.sort(comparator);
        }
        lastSortOrder = resultList.stream()
                .map(TodoModel::getId)
                .collect(Collectors.toList());

        return resultList;
    }
}
