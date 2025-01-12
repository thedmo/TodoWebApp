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

@Controller
@RequestMapping("/todos/edit")
public class EditTaskController {

    private TodoService todoService;
    private CategoryService categoryService;
    private AiCategoryService aiCategoryService;

    @Autowired
    public EditTaskController(TodoService todoService, CategoryService categoryService,
            AiCategoryService aiCategoryService) {
        this.todoService = todoService;
        this.categoryService = categoryService;
        this.aiCategoryService = aiCategoryService;
    }

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
