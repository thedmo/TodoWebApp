package hfu.java.todoapp.components.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hfu.java.todoapp.common.entities.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    CategoryEntity findByName (String name);
    CategoryEntity findById (int id);
}
