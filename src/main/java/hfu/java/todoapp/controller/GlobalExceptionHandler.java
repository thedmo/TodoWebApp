package hfu.java.todoapp.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the application.
 * Provides centralized exception handling across all controllers.
 * Handles unexpected errors and provides user-friendly error pages.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles all uncaught exceptions in the application.
     * Renders a generic error page with the exception details.
     * @param ex The caught exception
     * @param model Spring MVC model for passing error details to the view
     * @return The error view template name
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}