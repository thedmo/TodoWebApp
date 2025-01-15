package hfu.java.todoapp.common.models;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import hfu.java.todoapp.common.enums.Priority;

/**
 * Model class representing a Todo item in the application.
 * Contains all the business logic properties of a todo task.
 */
public class TodoModel {
    /** Unique identifier for the todo item */
    private Integer id;
    
    /** The actual task description */
    private String task;
    
    /** The category this todo belongs to */
    private CategoryModel category;
    
    /** Priority level of the todo */
    private Priority priority;
    
    /** Completion status of the todo */
    private boolean isDone;
    
    /** Due date for the todo item */
    private LocalDate dueDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    @DateTimeFormat(pattern ="yyyy-MM-dd")
    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
