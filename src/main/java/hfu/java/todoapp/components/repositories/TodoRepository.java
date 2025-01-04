package hfu.java.todoapp.components.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hfu.java.todoapp.common.entities.TodoEntity;

public interface TodoRepository extends JpaRepository<TodoEntity, Integer> {
    // Method to delete a todo by its ID
    void deleteById(Integer id);
}
