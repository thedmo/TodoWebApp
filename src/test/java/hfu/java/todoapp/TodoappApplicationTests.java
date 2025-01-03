package hfu.java.todoapp;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import hfu.java.todoapp.common.enums.Priority;
import hfu.java.todoapp.common.models.CategoryModel;
import hfu.java.todoapp.common.models.TodoModel;
import hfu.java.todoapp.components.services.CategoryService;
import hfu.java.todoapp.components.services.TodoService;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.parallel.ExecutionMode;


@SpringBootTest
@ActiveProfiles("test")
@Execution(ExecutionMode.SAME_THREAD) 
class TodoappApplicationTests {

	@Autowired
	private TodoService todoService;

	@Autowired
	private CategoryService categoryService;

	@BeforeAll
	static void createTestDatabase() {
		try (Connection connection = DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/postgres",
				"postgres",
				"postgres")) {

			Statement statement = connection.createStatement();

			statement.execute("SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'todoapp_test'");


			statement.execute("DROP DATABASE IF EXISTS todos_test");
			statement.execute("CREATE DATABASE todos_test");

		} catch (SQLException e) {
			throw new RuntimeException("Could not create test database", e);
		}
	}

	@Test
	void testCategoryUniquenes() {
		CategoryModel workCategory = new CategoryModel("Work", "Red");
		categoryService.save(workCategory);
		categoryService.save(workCategory);
		categoryService.save(workCategory);
		categoryService.save(workCategory);
		categoryService.save(workCategory);

		// Test category retrieval
		List<CategoryModel> allCategories = categoryService.getAll();
		assertNotNull(allCategories);

		var sameCategories = allCategories.stream()
		.filter(c -> c.getName().equals("Work"))
		.map(c -> c.getName())
		.toList();

		assertTrue(sameCategories.size() == 1);
	}

	@Test
	void testCategoryAndTodoOperations() {
		// Create test categories
		CategoryModel workCategory = new CategoryModel("Work", "Red");
		CategoryModel homeCategory = new CategoryModel("Home", "Blue");

		// Save categories
		categoryService.save(workCategory);
		categoryService.save(homeCategory);

		// Create todos
		TodoModel workTodo = new TodoModel();
		workTodo.setTask("Complete project");
		workTodo.setPriority(Priority.Priority_1);
		workTodo.setCategory(workCategory);
		todoService.save(workTodo);

		TodoModel homeTodo = new TodoModel();
		homeTodo.setTask("Clean house");
		homeTodo.setPriority(Priority.Priority_2);
		homeTodo.setCategory(homeCategory);
		todoService.save(homeTodo);

		// Test category retrieval
		List<CategoryModel> allCategories = categoryService.getAll();
		assertNotNull(allCategories);
		assertTrue(allCategories.size() >= 2);
		assertTrue(allCategories.stream()
				.filter(c -> c.getName().equals("Work"))
				.map(c -> c.getName())
				.toList()
				.size() == 1);
		assertTrue(allCategories.stream().anyMatch(c -> c.getName().equals("Home")));

		// Test todo retrieval
		List<TodoModel> allTodos = todoService.getAll();
		assertNotNull(allTodos);
		assertTrue(allTodos.size() >= 2);

		// Find specific todo and verify its properties
		TodoModel foundWorkTodo = allTodos.stream()
				.filter(t -> t.getTask().equals("Complete project"))
				.findFirst()
				.orElse(null);

		assertNotNull(foundWorkTodo);
		assertEquals(Priority.Priority_1, foundWorkTodo.getPriority());
		assertEquals("Work", foundWorkTodo.getCategory().getName());
		assertEquals("Red", foundWorkTodo.getCategory().getColor());

		// Find specific todo with different category
		TodoModel foundHomeTodo = allTodos.stream()
				.filter(t -> t.getTask().equals("Clean house"))
				.findFirst()
				.orElse(null);

		assertNotNull(foundHomeTodo);
		assertEquals(Priority.Priority_2, foundHomeTodo.getPriority());
		assertEquals("Home", foundHomeTodo.getCategory().getName());
		assertEquals("Blue", foundHomeTodo.getCategory().getColor());
	}
}
