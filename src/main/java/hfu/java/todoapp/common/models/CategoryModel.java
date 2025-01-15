package hfu.java.todoapp.common.models;

/**
 * Model class that represents a category in the application.
 */
public class CategoryModel {

    /** Unique identifier for the category */
    private Integer id = null;

    /** Name of the category */
    private String name;

    /** Color code for the category */
    private String color;

    /** Count of tasks using this category */
    private int usageCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public CategoryModel(String name) {
        super();
        this.name = name;
    }

    public CategoryModel(String name, String color) {
        super();
        this.name = name;
        this.color = color;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }
}
