package hfu.java.todoapp.mapper;

import hfu.java.todoapp.common.entities.CategoryEntity;
import hfu.java.todoapp.common.models.CategoryModel;

public class CategoryMapper {
    public static CategoryEntity getEntity (CategoryModel model){
        CategoryEntity entity = new CategoryEntity();
        if (model.getId() != null) {
            entity.setId(model.getId());
        }
        entity.setName(model.getName());
        entity.setColor(model.getColor());
        return entity;
    }

    public static CategoryModel getModel (CategoryEntity entity){
        CategoryModel model = new CategoryModel(entity.getName());
        model.setId(entity.getId());
        model.setColor(entity.getColor());
        model.setUsageCount(entity.getTodos().size());
        return model;
    }
}
