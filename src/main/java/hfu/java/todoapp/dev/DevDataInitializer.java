package hfu.java.todoapp.dev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import hfu.java.todoapp.common.enums.Priority;
import hfu.java.todoapp.common.models.CategoryModel;
import hfu.java.todoapp.common.models.TodoModel;
import hfu.java.todoapp.components.services.*;

@Component
@Profile("dev") // This ensures the runner only executes in development profile
public class DevDataInitializer implements CommandLineRunner {

    private final TodoService todoService;
    private final CategoryService categoryService;

    @Autowired
    public DevDataInitializer(TodoService todoService, CategoryService categoryService) {
        this.todoService = todoService;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeDevData();
    }

    private void initializeDevData() {
        // Creates categories
        CategoryModel foodCategory = new CategoryModel("Food", "Light green");
        categoryService.save(foodCategory);

        CategoryModel workCategory = new CategoryModel("Work", "Yellow");
        categoryService.save(workCategory);

        CategoryModel personalCategory = new CategoryModel("Personal", "Blue");
        categoryService.save(personalCategory);

        // Creates some todos
        TodoModel todo1 = new TodoModel();
        todo1.setTask("Clean living room");
        todo1.setPriority(Priority.Priority_3);
        todo1.setCategory(workCategory);
        todoService.save(todo1);

        TodoModel todo2 = new TodoModel();
        todo2.setTask("Buy groceries");
        todo2.setPriority(Priority.Priority_2);
        todo2.setCategory(foodCategory);
        todoService.save(todo2);

        TodoModel todo3 = new TodoModel();
        todo3.setTask("Finish Game");
        todo3.setPriority(Priority.Priority_5);
        todo3.setCategory(personalCategory);
        todoService.save(todo3);
    }
}
