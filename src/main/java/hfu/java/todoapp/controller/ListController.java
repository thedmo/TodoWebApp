package hfu.java.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hfu.java.todoapp.components.services.TodoService;

@Controller
public class ListController {
    private TodoService todoService;

    @Autowired
    public ListController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/")
    public String redirectList(Model model) {
        return "redirect:/todos/list";
    }

    @GetMapping("/todos/list")
    public String list(Model model) {
        model.addAttribute("todos", todoService.getAll());
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
        ;
        return "redirect:/todos/list";
    }
}