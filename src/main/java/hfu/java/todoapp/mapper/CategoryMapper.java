package hfu.java.todoapp.mapper;

import hfu.java.todoapp.common.entities.CategoryEntity;
import hfu.java.todoapp.common.models.CategoryModel;

/**
 * Mapper class for converting between Category entities and models.
 * Provides bidirectional mapping between persistence and business logic layers.
 */
public class CategoryMapper {
    /**
     * Converts a CategoryModel to a CategoryEntity.
     * @param model The model to convert
     * @return A new CategoryEntity with properties copied from the model
     */
    public static CategoryEntity getEntity (CategoryModel model){
        CategoryEntity entity = new CategoryEntity();
        if (model.getId() != null) {
            entity.setId(model.getId());
        }
        entity.setName(model.getName());
        entity.setColor(model.getColor());
        return entity;
    }

    /**
     * Converts a CategoryEntity to a CategoryModel.
     * @param entity The entity to convert
     * @return A new CategoryModel with properties copied from the entity
     */
    public static CategoryModel getModel (CategoryEntity entity){
        CategoryModel model = new CategoryModel(entity.getName());
        model.setId(entity.getId());
        model.setColor(entity.getColor());
        model.setUsageCount(entity.getTodos().size());
        return model;
    }
}
