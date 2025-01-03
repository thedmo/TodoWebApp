package hfu.java.todoapp.common.models;

public class CategoryModel {
    private Integer id = null;
    private String name;
    private String color;

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
}
