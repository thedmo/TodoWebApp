package hfu.java.todoapp.components.services;

import hfu.java.todoapp.common.models.CategoryModel;

/**
 * Service interface for AI-assisted category suggestions.
 * Provides functionality to automatically suggest appropriate categories
 * for tasks based on their descriptions using AI analysis.
 */

public interface AiCategoryService {

    /**
     * Requests an AI-suggested category for a given task description.
     * @param task The description of the task
     * @return A CategoryModel representing the suggested category
     */
    CategoryModel requestCategory(String task);

}
