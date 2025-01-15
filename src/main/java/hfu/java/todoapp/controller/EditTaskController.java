package hfu.java.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hfu.java.todoapp.common.models.TodoModel;
import hfu.java.todoapp.components.services.CategoryService;
import hfu.java.todoapp.components.services.TodoService;
import hfu.java.todoapp.components.services.AiCategoryService;

/**
 * Controller for handling todo task creation and editing.
 * Provides functionality for:
 * - Creating new todo tasks
 * - Editing existing tasks
 * - Managing task categories (manual and AI-assisted)
 * - Setting task properties (priority, due date, etc.)
 */
@Controller
@RequestMapping("/todos/edit")
public class EditTaskController {
    /** Service for managing todo operations */
    private final TodoService todoService;
    /** Service for managing categories */
    private final CategoryService categoryService;
    /** Service for AI-assisted category suggestions */
    private final AiCategoryService aiCategoryService;

    @Autowired
    public EditTaskController(TodoService todoService, CategoryService categoryService,
            AiCategoryService aiCategoryService) {
        this.todoService = todoService;
        this.categoryService = categoryService;
        this.aiCategoryService = aiCategoryService;
    }

    /**
     * Displays the task edit form.
     * If ID is provided, loads existing task for editing, otherwise prepares for new task creation.
     * @param id Optional ID of the task to edit
     * @param model Spring MVC model
     * @return The edit task view template name
     */
    @GetMapping("/")
    public String showEditPage(@RequestParam(value = "id", required = false, defaultValue = "-1") Integer id,
            Model model) {
        
        TodoModel todo;
        
        if (id == -1)
            todo = new TodoModel();
        else
            todo = todoService.getById(id);

        model.addAttribute("todo", todo);
        model.addAttribute("categories", categoryService.getAll());
        return "editTask";
    }

    /**
     * Processes the task edit form submission.
     * Handles both new task creation and existing task updates.
     * Supports both manual category selection and AI-assisted categorization.
     * @param task The todo model from the form
     * @param categoryId The selected category ID
     * @param aiCategory Whether to use AI for category suggestion
     * @return Redirects to the list view after successful save
     */
    @PostMapping("/update")
    public String saveTask(
            @ModelAttribute TodoModel task,
            @RequestParam(value = "categoryId", required = false, defaultValue = "-1") Integer categoryId,
            @RequestParam(value = "aiCategory", required = false, defaultValue = "false") boolean aiCategory,
            Model model) {

        if (aiCategory) {
            task.setCategory(aiCategoryService.requestCategory(task.getTask()));
        } else if (categoryId != -1 && !aiCategory)
            task.setCategory(categoryService.getById(categoryId));

        todoService.save(task);
        return "redirect:/todos/";
    }
}
