package hfu.java.todoapp;

import org.junit.jupiter.api.BeforeEach;
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
import hfu.java.todoapp.components.services.CategoryService;
import hfu.java.todoapp.components.services.TodoService;

import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SpringBootTest
@ActiveProfiles("test")
@Execution(ExecutionMode.SAME_THREAD)
class TodoappApplicationTests {

	private TodoService todoService;
	private CategoryService categoryService;
	private AiCategoryService aiCategoryService;

	@Autowired
	public TodoappApplicationTests(TodoService todoService, CategoryService categoryService, AiCategoryService aiCategoryService) {
		this.todoService = todoService;
		this.categoryService = categoryService;
		this.aiCategoryService = aiCategoryService;
	}

	@BeforeEach
	void initializeTables() {
		todoService.deleteAllEntries();
		categoryService.deleteAllEntries();
	}

	@Test
	void testCategoryUniquenes() {
		initializeTables();
		CategoryModel workCategory = new CategoryModel("Work", "#FF0000");
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
		initializeTables();
		
		// Create test categories with consistent color values
		String workColor = "#FF5733";
		String homeColor = "#0000FF";
		CategoryModel workCategory = new CategoryModel("Work", workColor);
		CategoryModel homeCategory = new CategoryModel("Home", homeColor);

		// Save categories
		workCategory = categoryService.save(workCategory);  // Store the returned category
		homeCategory = categoryService.save(homeCategory);  // Store the returned category

		// Create todos
		String task1String = "testCategoryAndTodoOperations: Complete project";
		TodoModel workTodo = new TodoModel();
		workTodo.setTask(task1String);
		workTodo.setPriority(Priority.Priority_1);
		workTodo.setCategory(workCategory);
		todoService.save(workTodo);

		String task2String = "testCategoryAndTodoOperations: Clean house";
		TodoModel homeTodo = new TodoModel();
		homeTodo.setTask(task2String);
		homeTodo.setPriority(Priority.Priority_2);
		homeTodo.setCategory(homeCategory);
		todoService.save(homeTodo);

		// Test category retrieval
		List<CategoryModel> allCategories = categoryService.getAll();
		assertNotNull(allCategories);
		assertEquals(2, allCategories.size());

		// Test todo retrieval
		List<TodoModel> allTodos = todoService.getAll();
		assertNotNull(allTodos);
		assertEquals(2, allTodos.size());

		// Find specific todo and verify its properties
		TodoModel foundWorkTodo = allTodos.stream()
				.filter(t -> t.getTask().equals(task1String))
				.findFirst()
				.orElse(null);

		assertNotNull(foundWorkTodo);
		assertEquals(Priority.Priority_1, foundWorkTodo.getPriority());
		assertEquals("Work", foundWorkTodo.getCategory().getName());
		assertEquals(workColor, foundWorkTodo.getCategory().getColor());

		// Find specific todo with different category
		TodoModel foundHomeTodo = allTodos.stream()
				.filter(t -> t.getTask().equals(task2String))
				.findFirst()
				.orElse(null);

		assertNotNull(foundHomeTodo);
		assertEquals(Priority.Priority_2, foundHomeTodo.getPriority());
		assertEquals("Home", foundHomeTodo.getCategory().getName());
		assertEquals(homeColor, foundHomeTodo.getCategory().getColor());
	}

	@Test
	void testUpdateTodo() {
		initializeTables();
		// Create and save a new category
		CategoryModel category = new CategoryModel("Work", "#FF0000");
		categoryService.save(category);

		String taskString = "testUpdateTodo: Complete project";
		String udpatedTaskString = "testUpdateTodo: Complete project - Updated";

		// Create a new TodoModel
		TodoModel todo = new TodoModel();
		todo.setTask(taskString);
		todo.setPriority(Priority.Priority_1);
		todo.setCategory(category);

		// Save the todo
		TodoModel savedTodo = todoService.save(todo);
		assertNotNull(savedTodo);
		assertEquals(taskString, savedTodo.getTask());

		// Retrieve the saved todo
		TodoModel retrievedTodo = todoService.getAll().stream()
				.filter(t -> t.getId() == savedTodo.getId())
				.findFirst()
				.orElse(null);
		assertNotNull(retrievedTodo);

		// Store the original ID for later comparison
		int originalId = retrievedTodo.getId();

		// Change some properties
		retrievedTodo.setTask(udpatedTaskString);
		retrievedTodo.setPriority(Priority.Priority_2);

		// Save the updated todo
		TodoModel updatedTodo = todoService.save(retrievedTodo);

		// Retrieve the updated todo
		TodoModel finalRetrievedTodo = todoService.getAll().stream()
				.filter(t -> t.getId() == updatedTodo.getId())
				.findFirst()
				.orElse(null);

		// Check if the properties were updated correctly
		assertNotNull(finalRetrievedTodo);
		assertEquals(udpatedTaskString, finalRetrievedTodo.getTask());
		assertEquals(Priority.Priority_2, finalRetrievedTodo.getPriority());

		// Check that the ID remains the same (not a new entry)
		assertEquals(originalId, finalRetrievedTodo.getId());
	}

	@Test
	public void testAddAndCompleteTasks() {
		initializeTables();
		// Create and save a new category
		CategoryModel category = new CategoryModel("Personal", "#0000FF");
		categoryService.save(category);

		// Create and save multiple TodoModels
		String taskPrefix = "testAddAndCompleteTasks: ";
		List<TodoModel> todos = new ArrayList<>();
		for (int i = 1; i <= 6; i++) {
			TodoModel todo = new TodoModel();
			todo.setTask(taskPrefix + "Task " + i);
			todo.setPriority(i % 2 == 0 ? Priority.Priority_2 : Priority.Priority_1);
			todo.setCategory(category);
			todos.add(todoService.save(todo));
		}

		// Retrieve the saved todos with the specified prefix
		List<TodoModel> retrievedTodos = todoService.getAll().stream()
				.filter(t -> t.getTask().startsWith(taskPrefix))
				.collect(Collectors.toList());
		assertEquals(6, retrievedTodos.size());

		// Mark half of them as completed
		for (int i = 0; i < retrievedTodos.size() / 2; i++) {
			TodoModel todoToUpdate = retrievedTodos.get(i);
			todoToUpdate.setDone(true);
			todoService.save(todoToUpdate);
		}

		// Verify that the updates were successful
		List<TodoModel> updatedTodos = todoService.getCompleted();
		for (TodoModel todo : updatedTodos) {
			assertTrue(todo.isDone());
		}
	}

	@Test
    void testRequestCategory() throws Exception {
        initializeTables();
        String task = "Complete the project";

		CategoryModel cat = aiCategoryService.requestCategory(task);

		CategoryModel savedCategory = categoryService.getById(cat.getId());


        assertNotNull(savedCategory);
		assertEquals(cat.getName(), savedCategory.getName());
		assertEquals(cat.getColor(), savedCategory.getColor());
    }
}
