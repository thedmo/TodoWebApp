package hfu.java.todoapp.dev;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import hfu.java.todoapp.common.enums.Priority;
import hfu.java.todoapp.common.models.CategoryModel;
import hfu.java.todoapp.common.models.TodoModel;
import hfu.java.todoapp.components.services.*;

@Component
@Profile("dev")
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
        CategoryModel foodCategory = new CategoryModel("Food", "#90EE90"); // Light green
        categoryService.save(foodCategory);
        
        CategoryModel workCategory = new CategoryModel("Work", "#FFFF00"); // Yellow
        categoryService.save(workCategory);
        
        CategoryModel personalCategory = new CategoryModel("Personal", "#ADD8E6"); // Blue
        categoryService.save(personalCategory);
        
        CategoryModel healthCategory = new CategoryModel("Health", "#FF6347"); // Red
        categoryService.save(healthCategory);
        
        CategoryModel educationCategory = new CategoryModel("Education", "#9370DB"); // Purple
        categoryService.save(educationCategory);
        
        // Creates some todos
        TodoModel todo1 = new TodoModel();
        todo1.setTask("Clean living room");
        todo1.setPriority(Priority.Priority_3);
        todo1.setCategory(workCategory);
        todo1.setDueDate(LocalDate.parse("2023-10-15"));
        todoService.save(todo1);
        
        TodoModel todo2 = new TodoModel();
        todo2.setTask("Buy groceries");
        todo2.setPriority(Priority.Priority_2);
        todo2.setCategory(foodCategory);
        todo2.setDueDate(LocalDate.parse("2023-10-10"));
        todoService.save(todo2);
        
        TodoModel todo3 = new TodoModel();
        todo3.setTask("Finish Game");
        todo3.setPriority(Priority.Priority_5);
        todo3.setCategory(personalCategory);
        todo3.setDueDate(LocalDate.parse("2023-10-20"));
        todoService.save(todo3);
        
        TodoModel todo4 = new TodoModel();
        todo4.setTask("Go for a run");
        todo4.setPriority(Priority.Priority_4);
        todo4.setCategory(healthCategory);
        todo4.setDueDate(LocalDate.parse( "2023-10-18"));
        todoService.save(todo4);

        TodoModel todo5 = new TodoModel();
        todo5.setTask("Study for exam");
        todo5.setPriority(Priority.Priority_1);
        todo5.setCategory(educationCategory);
        todoService.save(todo5);

        TodoModel todo6 = new TodoModel();
        todo6.setTask("Prepare presentation");
        todo6.setPriority(Priority.Priority_2);
        todo6.setCategory(workCategory);
        todoService.save(todo6);

        TodoModel todo7 = new TodoModel();
        todo7.setTask("Call mom");
        todo7.setPriority(Priority.Priority_3);
        todo7.setCategory(personalCategory);
        todoService.save(todo7);

        TodoModel todo8 = new TodoModel();
        todo8.setTask("Plan weekend trip");
        todo8.setPriority(Priority.Priority_4);
        todo8.setCategory(personalCategory);
        todoService.save(todo8);

        TodoModel todo9 = new TodoModel();
        todo9.setTask("Schedule dentist appointment");
        todo9.setPriority(Priority.Priority_3);
        todo9.setCategory(healthCategory);
        todoService.save(todo9);

        TodoModel todo10 = new TodoModel();
        todo10.setTask("Read chapter 5");
        todo10.setPriority(Priority.Priority_2);
        todo10.setCategory(educationCategory);
        todoService.save(todo10);
    }
}
