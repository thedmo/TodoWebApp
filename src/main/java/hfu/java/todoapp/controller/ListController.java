package hfu.java.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import hfu.java.todoapp.components.services.TodoService;

@Controller
public class ListController {
    private final TodoService todoService;
    private int sortColumn = 0;
    private boolean ascending = true;

    @Autowired
    public ListController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/")
    public String redirectList(Model model) {
        return "redirect:/todos/list";
    }

    @GetMapping("/todos/list")
    public String list(@RequestParam(value = "keepOrder", required = false, defaultValue = "true") boolean keepOrder, Model model) {
        model.addAttribute("todos", todoService.getAllSorted(sortColumn, ascending, keepOrder));
        model.addAttribute("sortColumn", sortColumn);
        model.addAttribute("ascending", ascending);
        return "list";
    }

    @PostMapping("/todos/updateStatus")
    public String updateTodoStatus(@RequestParam("id") Integer id,
            @RequestParam(value = "isDone", required = false) boolean isDone) {
        todoService.updateTodoStatus(id, isDone);
        return "redirect:/todos/list";
    }

    @PostMapping("/todos/delete")
    public String deleteTodo(@RequestParam("id") Integer id) {
        todoService.deleteTodoById(id);
        return "redirect:/todos/list";
    }

    @PostMapping("/todos/updateSort")
    public String updateSort(@RequestParam("column") int column, 
                            @RequestParam("ascending") boolean ascending, Model model) {

        this.ascending = sortColumn == column ? !ascending : ascending;
        this.sortColumn = column;
        return "redirect:/todos/list?keepOrder=false";
    }
}