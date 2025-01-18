package hfu.java.todoapp.common.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a Category in the database.
 * Maps to the 'category' table in the database.
 * A Category can be assigned to multiple Todo items and contains properties
 * like name and color.
 */
@Entity
@Table(name = "category")
public class CategoryEntity {

    /**
     * The unique identifier for the category.
     * Auto-generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The name of the category.
     * Must be unique across all categories.
     */
    @Column(name = "name", unique = true)
    private String name;

    /**
     * The color associated with the category.
     * Used for visual representation.
     */
    @Column(name = "color")
    private String color;

    /**
     * Collection of Todo items associated with this category.
     * Eagerly fetched when the category is loaded.
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private Set<TodoEntity> todos = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<TodoEntity> getTodos() {
        return todos;
    }
}
