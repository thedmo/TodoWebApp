package hfu.java.todoapp.components.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import hfu.java.todoapp.common.entities.*;
import hfu.java.todoapp.common.models.CategoryModel;
import hfu.java.todoapp.common.models.TodoModel;
import hfu.java.todoapp.components.repositories.CategoryRepository;
import hfu.java.todoapp.components.repositories.TodoRepository;
import hfu.java.todoapp.mapper.CategoryMapper;
import hfu.java.todoapp.mapper.TodoMapper;
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
}
