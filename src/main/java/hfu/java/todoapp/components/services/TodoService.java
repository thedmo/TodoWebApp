package hfu.java.todoapp.components.services;

import java.util.List;
import org.springframework.stereotype.Service;

import hfu.java.todoapp.common.entities.*;
import hfu.java.todoapp.common.models.CategoryModel;
import hfu.java.todoapp.common.models.TodoModel;
import hfu.java.todoapp.components.repositories.CategoryRepository;
import hfu.java.todoapp.components.repositories.TodoRepository;
import hfu.java.todoapp.mapper.CategoryMapper;
import hfu.java.todoapp.mapper.TodoMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;
    private List<TodoModel> todos;

    public TodoService(TodoRepository todoRepository, CategoryRepository categoryRepository) {
        this.todoRepository = todoRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public TodoModel save(TodoModel model) {
        // Find or create the category
        CategoryEntity categoryEntity = categoryRepository.findByName(model.getCategory().getName());

        // Map the todo
        TodoEntity todoEntity = TodoMapper.map(model);
        todoEntity.setCategory(categoryEntity);

        // Save the todo
        TodoEntity savedEntity = todoRepository.save(todoEntity);

        // Refresh the cached list
        refreshTodos();

        // Return the saved model
        TodoModel savedModel = TodoMapper.getModel(savedEntity);
        savedModel.setCategory(CategoryMapper.getModel(savedEntity.getCategory()));
        return savedModel;
    }

    public List<TodoModel> getAll() {
        if (todos == null) {
            refreshTodos();
        }
        return todos;
    }

    public List<TodoModel> getCompleted() {
        return getAll()
                .stream()
                .filter(TodoModel::isDone)
                .toList();
    }

    @Transactional
    private void refreshTodos() {
        todos = todoRepository.findAll()
                .stream()
                .map(e -> {
                    TodoModel m = TodoMapper.getModel(e);
                    CategoryModel c = CategoryMapper.getModel(e.getCategory());
                    m.setCategory(c);
                    return m;
                })
                .toList();
    }

    @Transactional
    public void deleteTodoById(Integer id) {
        todoRepository.deleteById(id);
        refreshTodos();
    }

    @Transactional
    public void deleteAllEntries() {
        todoRepository.deleteAll();
        todos.clear();
    }

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
}
