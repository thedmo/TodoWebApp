package hfu.java.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hfu.java.todoapp.common.models.TodoModel;
import hfu.java.todoapp.components.services.TodoService;
import java.util.List;

@Controller
@RequestMapping("/todos")
public class ListController {
    private final TodoService todoService;
    private int sortColumn = 0;
    private boolean ascending = true;
    private boolean isFiltered = false;

    @Autowired
    public ListController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/")
    public String redirectList(Model model) {
        return "redirect:/todos/list";
    }

    @GetMapping("/list")
    public String list(@RequestParam(value = "keepOrder", required = false, defaultValue = "true") boolean keepOrder,
            Model model) {
        List<TodoModel> todos = todoService.getSorted(sortColumn, ascending, keepOrder, isFiltered);
        model.addAttribute("todos", todos);
        model.addAttribute("sortColumn", sortColumn);
        model.addAttribute("ascending", ascending);
        model.addAttribute("isFiltered", isFiltered);
        model.addAttribute("allTasksDone", todos.isEmpty() || todos.stream().allMatch(TodoModel::isDone));
        return "list";
    }

    @PostMapping("/filter")
    public String filterList(@RequestParam(value = "isFiltered", required = false, defaultValue = "false") boolean isFiltered, Model model) {
        this.isFiltered = isFiltered;
        return "redirect:/todos/list";
    }

    @PostMapping("/update")
    public String updateTodoStatus(@RequestParam("id") Integer id,
            @RequestParam(value = "isDone", required = false) boolean isDone) {
        todoService.updateTodoStatus(id, isDone);
        return "redirect:/todos/list";
    }

    @PostMapping("/delete")
    public String deleteTodo(@RequestParam("id") Integer id) {
        todoService.deleteTodoById(id);
        return "redirect:/todos/list";
    }

    @PostMapping("/sort")
    public String updateSort(@RequestParam("column") int column,
            @RequestParam("ascending") boolean ascending, Model model) {

        this.ascending = sortColumn == column ? !ascending : ascending;
        this.sortColumn = column;
        return "redirect:/todos/list?keepOrder=false";
    }
}