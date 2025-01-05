package hfu.java.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import hfu.java.todoapp.common.models.CategoryModel;
import hfu.java.todoapp.components.services.CategoryService;

@Controller
@RequestMapping("/todos/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String showManagePage(Model model) {
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("categoryService", categoryService);
        return "categoriesList";
    }

    @PostMapping("/add")
    public String addCategory(@RequestParam String name, @RequestParam String color) {
        CategoryModel newCategory = new CategoryModel(name, color);
        categoryService.save(newCategory);
        return "redirect:/todos/categories/list";
    }

    @PostMapping("/updateColor")
    public String updateColor(@RequestParam Integer id, @RequestParam String color) {
        CategoryModel category = categoryService.getById(id);
        category.setColor(color);
        categoryService.save(category);
        return "redirect:/todos/categories/list";
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestParam Integer id) {
        if (categoryService.countTodos(id) == 0) {
            categoryService.deleteCategoryById(id);
        }
        return "redirect:/todos/categories/list";
    }
} 