package hfu.java.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hfu.java.todoapp.common.models.TodoModel;
import hfu.java.todoapp.components.services.*;
import java.util.List;

/**
 * Controller handling todo list operations and views.
 * Manages the main todo list page, including sorting, filtering, and basic CRUD operations.
 * Provides endpoints for:
 * - Viewing the todo list
 * - Updating todo status
 * - Deleting completed todos
 * - Sorting and filtering todos
 */
@Controller
@RequestMapping("/todos")
public class ListController {
    /** Service for managing todo operations */
    private final TodoService todoService;
    
    /** Current column being used for sorting (0=Done, 1=Task, 2=Category, 3=DueDate, 4=Priority) */
    private int sortColumn = 0;
    
    /** Current sort direction */
    private boolean ascending = true;
    
    /** Whether to show only non-completed tasks */
    private boolean isFiltered = false;

    @Autowired
    public ListController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * Redirects root todo path to the list view.
     * @param model Spring MVC model
     * @return Redirect string to the list view
     */
    @GetMapping("/")
    public String redirectList(Model model) {
        return "redirect:/todos/list";
    }

    /**
     * Displays the main todo list view.
     * Shows all todos or only pending ones based on filter status.
     * @param keepOrder Whether to maintain the current sort order
     * @param model Spring MVC model for view data
     * @return The list view template name
     */
    @GetMapping("/list")
    public String list(@RequestParam(value = "keepOrder", required = false, defaultValue = "true") boolean keepOrder,
            Model model) {
        List<TodoModel> todos = todoService.getSorted(sortColumn, ascending, keepOrder, isFiltered);
        model.addAttribute("todos", todos);
        model.addAttribute("sortColumn", sortColumn);
        model.addAttribute("ascending", ascending);
        model.addAttribute("isFiltered", isFiltered);
        model.addAttribute("allTasksDone", todos.isEmpty() || todos.stream().allMatch(TodoModel::isDone));
        model.addAttribute("pendingCount", todoService.getPending().size());
        return "list";
    }

    /**
     * Toggles the filter for showing/hiding completed tasks.
     * @param isFiltered Whether to show only non-completed tasks
     * @param model Spring MVC model
     * @return Redirect string to the list view
     */
    @PostMapping("/filter")
    public String filterList(@RequestParam(value = "isFiltered", required = false, defaultValue = "false") boolean isFiltered, Model model) {
        this.isFiltered = isFiltered;
        return "redirect:/todos/list";
    }

    /**
     * Updates the completion status of a todo item.
     * @param id The ID of the todo to update
     * @param isDone The new completion status
     * @return Redirects to the list view
     */
    @PostMapping("/update")
    public String updateTodoStatus(@RequestParam("id") Integer id,
            @RequestParam(value = "isDone", required = false) boolean isDone) {
        todoService.updateTodoStatus(id, isDone);
        return "redirect:/todos/list";
    }

    /**
     * Deletes a todo item.
     * Only completed todos can be deleted.
     * @param id The ID of the todo to delete
     * @return Redirects to the list view
     */
    @PostMapping("/delete")
    public String deleteTodo(@RequestParam("id") Integer id) {
        todoService.deleteTodoById(id);
        return "redirect:/todos/list";
    }

    /**
     * Updates the sort order of the todo list.
     * Toggles sort direction if the same column is selected twice.
     * @param column The column index to sort by
     * @param ascending Whether to sort in ascending order
     * @param model Spring MVC model
     * @return Redirects to the list view with updated sort parameters
     */
    @PostMapping("/sort")
    public String updateSort(@RequestParam("column") int column,
            @RequestParam("ascending") boolean ascending, Model model) {
        this.ascending = sortColumn == column ? !ascending : ascending;
        this.sortColumn = column;
        return "redirect:/todos/list?keepOrder=false";
    }
}