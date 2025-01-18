package hfu.java.todoapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hfu.java.todoapp.common.enums.Priority;
import hfu.java.todoapp.common.models.CategoryModel;
import hfu.java.todoapp.common.models.TodoModel;
import hfu.java.todoapp.components.services.AiCategoryService;
import hfu.java.todoapp.components.services.CategoryServiceImpl;
import hfu.java.todoapp.components.services.TodoServiceImpl;

import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;

@SpringBootTest
@ActiveProfiles("test")
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
class TodoappApplicationTests {

	private TodoServiceImpl todoService;
	private CategoryServiceImpl categoryService;
	private AiCategoryService aiCategoryService;

	@Autowired
	public TodoappApplicationTests(TodoServiceImpl todoService, CategoryServiceImpl categoryService, AiCategoryService aiCategoryService) {
		this.todoService = todoService;
		this.categoryService = categoryService;
		this.aiCategoryService = aiCategoryService;
	}

	@BeforeEach
	void initializeTables() {
		todoService.deleteAllEntries();
		categoryService.deleteAllEntries();
	}

	/**
	 * Tests that categories with the same name are stored only once in the database
	 */
	@Test
	@Order(1)
	void testCategoryUniquenes() {
		initializeTables();
		CategoryModel workCategory = new CategoryModel("Work", "#FF0000");
		categoryService.save(workCategory);
		categoryService.save(workCategory);
		categoryService.save(workCategory);
		categoryService.save(workCategory);
		categoryService.save(workCategory);

		List<CategoryModel> allCategories = categoryService.getAll();
		assertNotNull(allCategories);

		var sameCategories = allCategories.stream()
				.filter(c -> c.getName().equals("Work"))
				.map(c -> c.getName())
				.toList();

		assertTrue(sameCategories.size() == 1);
	}

	/**
	 * Tests updating a todo's properties while maintaining its ID and relationships
	 */
	@Test
	@Order(3)
	void testUpdateTodo() {
		initializeTables();
		CategoryModel category = new CategoryModel("Work", "#FF0000");
		categoryService.save(category);

		String taskString = "testUpdateTodo: Complete project";
		String udpatedTaskString = "testUpdateTodo: Complete project - Updated";

		TodoModel todo = new TodoModel();
		todo.setTask(taskString);
		todo.setPriority(Priority.Priority_1);
		todo.setCategory(category);

		TodoModel savedTodo = todoService.save(todo);
		assertNotNull(savedTodo);
		assertEquals(taskString, savedTodo.getTask());

		TodoModel retrievedTodo = todoService.getAll().stream()
				.filter(t -> t.getId() == savedTodo.getId())
				.findFirst()
				.orElse(null);
		assertNotNull(retrievedTodo);

		int originalId = retrievedTodo.getId();

		retrievedTodo.setTask(udpatedTaskString);
		retrievedTodo.setPriority(Priority.Priority_2);

		TodoModel updatedTodo = todoService.save(retrievedTodo);

		TodoModel finalRetrievedTodo = todoService.getAll().stream()
				.filter(t -> t.getId() == updatedTodo.getId())
				.findFirst()
				.orElse(null);

		assertNotNull(finalRetrievedTodo);
		assertEquals(udpatedTaskString, finalRetrievedTodo.getTask());
		assertEquals(Priority.Priority_2, finalRetrievedTodo.getPriority());

		assertEquals(originalId, finalRetrievedTodo.getId());
	}

	/**
	 * Tests adding multiple tasks and marking them as complete, verifying the pending tasks filter
	 */
	@Test
	@Order(4)
	void testAddAndCompleteTasks() {
		initializeTables();
		CategoryModel category = new CategoryModel("Personal", "#0000FF");
		categoryService.save(category);

		String taskPrefix = "testAddAndCompleteTasks: ";
		List<TodoModel> todos = new ArrayList<>();
		for (int i = 1; i <= 6; i++) {
			TodoModel todo = new TodoModel();
			todo.setTask(taskPrefix + "Task " + i);
			todo.setPriority(i % 2 == 0 ? Priority.Priority_2 : Priority.Priority_1);
			todo.setCategory(category);
			todos.add(todoService.save(todo));
		}

		List<TodoModel> retrievedTodos = todoService.getAll().stream()
				.filter(t -> t.getTask().startsWith(taskPrefix))
				.collect(Collectors.toList());
		assertEquals(6, retrievedTodos.size());

		for (int i = 0; i < retrievedTodos.size() / 2; i++) {
			TodoModel todoToUpdate = retrievedTodos.get(i);
			todoToUpdate.setDone(true);
			todoService.save(todoToUpdate);
		}

		List<TodoModel> updatedTodos = todoService.getPending();
		for (TodoModel todo : updatedTodos) {
			assertTrue(!todo.isDone());
		}
	}

	/**
	 * Tests the AI category suggestion functionality by requesting a category for a given task
	 */
	@Test
	@Order(5)
	void testRequestCategory() throws Exception {
		initializeTables();
		String task = "Complete the project";

		CategoryModel cat = aiCategoryService.requestCategory(task);

		CategoryModel savedCategory = categoryService.getById(cat.getId());

		assertNotNull(savedCategory);
		assertEquals(cat.getName(), savedCategory.getName());
		assertEquals(cat.getColor(), savedCategory.getColor());
	}

	/**
	 * Tests the accurate counting of todos per category, including add and delete operations
	 */
	@Test
	@Order(6)
	void testCategoryCount() {
		initializeTables();
		
		CategoryModel workCategory = new CategoryModel("Work", "#FF5733");
		CategoryModel homeCategory = new CategoryModel("Home", "#0000FF");
		
		workCategory = categoryService.save(workCategory);
		homeCategory = categoryService.save(homeCategory);
		
		for (int i = 1; i <= 3; i++) {
			TodoModel todo = new TodoModel();
			todo.setTask("Work Task " + i);
			todo.setPriority(Priority.Priority_1);
			todo.setCategory(workCategory);
			todoService.save(todo);
		}
		
		for (int i = 1; i <= 2; i++) {
			TodoModel todo = new TodoModel();
			todo.setTask("Home Task " + i);
			todo.setPriority(Priority.Priority_2);
			todo.setCategory(homeCategory);
			todoService.save(todo);
		}
		
		assertEquals(3, categoryService.countTodos(workCategory.getId()));
		assertEquals(2, categoryService.countTodos(homeCategory.getId()));
		
		TodoModel extraTodo = new TodoModel();
		extraTodo.setTask("Extra Work Task");
		extraTodo.setPriority(Priority.Priority_1);
		extraTodo.setCategory(workCategory);
		todoService.save(extraTodo);
		
		assertEquals(4, categoryService.countTodos(workCategory.getId()));
		
		TodoModel todoToDelete = todoService.getAll().stream()
				.filter(t -> t.getTask().equals("Work Task 1"))
				.findFirst()
				.orElseThrow();
		
		todoService.deleteTodoById(todoToDelete.getId());
		
		assertEquals(3, categoryService.countTodos(workCategory.getId()));
	}
}
