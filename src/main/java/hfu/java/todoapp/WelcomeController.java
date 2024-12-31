package hfu.java.todoapp;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WelcomeController {

    private ArrayList<String> list = new ArrayList<>();

    @GetMapping("/addItem")
    public String showAddPage() {
        return "addItem";
    }

    @PostMapping("/addNew")
    public String addItem(@RequestParam("newItem") String newItem, Model model) {
        list.add(newItem);

        model.addAttribute("dataList", list);
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String welcome(Model model) {
        // Add data to the model
        model.addAttribute("dataList", list);
        return "welcome";
    }
}