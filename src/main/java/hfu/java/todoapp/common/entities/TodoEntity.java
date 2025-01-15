package hfu.java.todoapp.common.entities;

import java.time.LocalDate;


import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

/**
 * Entity class representing a Todo item in the database.
 * Maps to the 'todo' table in the database.
 */
@Entity
@Table (name = "todo")
public class TodoEntity {

    /** Auto-generated primary key */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** The task description */
    @Column(name = "todo_task", nullable = false)
    @Nonnull
    private String task;

    /** Whether the task is completed */
    @Column(name = "done", nullable = false)
    @Nonnull
    private boolean isDone = false;

    /** Optional due date for the task */
    @Column(name = "duedate", nullable = true)
    @Nullable
    private LocalDate dueDate;

    /** Priority level of the task */
    @Column(name = "priority")
    private int priority;

    /** Category relationship */
    @ManyToOne
    @JoinColumn(name ="category_id")
    private CategoryEntity category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}
