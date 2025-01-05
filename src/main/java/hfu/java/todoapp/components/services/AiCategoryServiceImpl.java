package hfu.java.todoapp.components.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import hfu.java.todoapp.common.models.CategoryModel;

@Service
@Order(Ordered.LOWEST_PRECEDENCE)
public class AiCategoryServiceImpl implements AiCategoryService {

    private ChatModel chatModel;
    private CategoryService categoryService;

    @Autowired
    public AiCategoryServiceImpl(@Qualifier("OpenAi") ChatModel chatModel, CategoryService categoryService) {
        this.chatModel = chatModel;
        this.categoryService = categoryService;
    }

    @Override
    public CategoryModel requestCategory(String task) {
        StringBuilder prompStringBuilder = new StringBuilder();
        prompStringBuilder
                .append("Find a matching category for the following task: ")
                .append(task)
                .append(". Category must consist of exactly one word and must not have special characters. ")
                .append("also add a matching color with it in form of a hex value (to be used with thymeleaf). ")
                .append("answer in exact following schema: ")
                .append("name=<category>:color=<color>")
                .append("do only respond with the given schema. ");

        String promptString = prompStringBuilder.toString();
        String newCategoryString = "";

        chatModel.call(promptString);
        try {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                Prompt prompt = new Prompt(promptString);

                ChatResponse response = chatModel.call(prompt);
                return response.getResult().getOutput().getContent();
            });

            newCategoryString = future.get();

        } 
        catch (Exception e) {
            e.printStackTrace();
        }

        var values = newCategoryString.split(":");
        var catString = values[0].replace("name=", "");
        var colstring = values[1].replace("color=", "");

        CategoryModel newCatModel = new CategoryModel(catString);
        newCatModel.setColor(colstring);

        return categoryService.save(newCatModel);
    }
}
