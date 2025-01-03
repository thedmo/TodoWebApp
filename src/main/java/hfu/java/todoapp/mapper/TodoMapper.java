package hfu.java.todoapp.mapper;

import hfu.java.todoapp.common.entities.TodoEntity;
import hfu.java.todoapp.common.enums.Priority;
import hfu.java.todoapp.common.models.TodoModel;

public class TodoMapper {

    public static TodoEntity map(TodoModel model){
        TodoEntity entity = new TodoEntity();
        if (model.getId() != null) {
            entity.setId(model.getId());
        }
        entity.setTask(model.getTask());
        entity.setPriority(model.getPriority().getValue());
        entity.setDueDate(model.getDueDate());
        entity.setIsDone(model.isDone());

        return entity;
    }

    public static TodoModel getModel(TodoEntity entity){
        TodoModel model = new TodoModel();
        model.setId(entity.getId());
        model.setTask(entity.getTask());
        model.setPriority(Priority.getByInt( entity.getPriority()));
        model.setDueDate(entity.getDueDate());
        model.setDone(entity.getIsDone());

        return model;
    }

}
