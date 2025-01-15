package hfu.java.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import hfu.java.todoapp.common.models.CategoryModel;
import hfu.java.todoapp.components.services.CategoryService;

/**
 * Controller for managing categories in the application.
 * Provides functionality for:
 * - Viewing all categories
 * - Creating new categories
 * - Deleting existing categories
 * - Viewing category usage statistics
 */
@Controller
@RequestMapping("/todos/categories")
public class CategoryController {

    /** Service for managing category operations */
    private final CategoryService categoryService;

    /** Default sort column (completed) */
    private int sortColumn = 0;

    /** Sorting direction */
    private boolean ascending = true;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Displays the list of all categories.
     * Shows category details and their usage counts.
     * @param model Spring MVC model
     * @return The categories list view template name
     */
    @GetMapping("/list")
    public String showCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllSorted(sortColumn, ascending));
        model.addAttribute("sortColumn", sortColumn);
        model.addAttribute("ascending", ascending);
        return "categoriesList";
    }

    /**
     * Creates a new category.
     * @param name The name of the new category
     * @param color The color code for the new category
     * @return Redirects to the categories list view
     */
    @PostMapping("/add")
    public String addCategory(@RequestParam String name, @RequestParam String color) {
        CategoryModel newCategory = new CategoryModel(name, color);
        categoryService.save(newCategory);
        return "redirect:/todos/categories/list";
    }

    /**
     * Deletes an existing category.
     * Only allows deletion if the category is not in use.
     * @param id The ID of the category to delete
     * @return Redirects to the categories list view
     */
    @PostMapping("/delete")
    public String deleteCategory(@RequestParam Integer id) {
        if (categoryService.countTodos(id) == 0) {
            categoryService.deleteCategoryById(id);
        }
        return "redirect:/todos/categories/list";
    }

    /**
     * Updates the color of a category.
     * @param id The ID of the category to update
     * @param color The new color code
     * @return Redirects to the categories list page
     */
    @PostMapping("/updateColor")
    public String updateColor(@RequestParam Integer id, @RequestParam String color) {
        CategoryModel category = categoryService.getById(id);
        category.setColor(color);
        categoryService.save(category);
        return "redirect:/todos/categories/list";
    }

    /**
     * Updates the sorting configuration.
     * @param column The column to sort by
     * @param ascending The sorting direction
     * @return Redirects to the categories list page
     */
    @PostMapping("/updateSort")
    public String updateSort(@RequestParam("column") int column,
            @RequestParam("ascending") boolean ascending) {
        this.sortColumn = column;
        this.ascending = ascending;
        return "redirect:/todos/categories/list";
    }
}