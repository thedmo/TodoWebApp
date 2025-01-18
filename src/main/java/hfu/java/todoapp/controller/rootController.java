package hfu.java.todoapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller handling the root path and redirects to the todo list.
 */
@Controller
public class rootController {

    /**
     * Redirects the root path to the todo list.
     * @return Redirect string to the todo list
     */
    @GetMapping("/")
    public String redirectList() {
        return "redirect:/todos/list";
    }
}
