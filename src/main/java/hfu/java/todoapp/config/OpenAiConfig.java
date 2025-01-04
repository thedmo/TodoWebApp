package hfu.java.todoapp.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAI integration.
 * Sets up the ChatGPT model with specific parameters.
 */
@Configuration
public class OpenAiConfig {

    /**
     * Configures OpenAI chat options with specific model and temperature settings.
     *
     * @return OpenAiChatOptions configured for GPT-4
     */
    @Bean
    public OpenAiChatOptions openAiChatOptions() {
        return OpenAiChatOptions
                .builder()
                .withModel("gpt-4o")
                .withTemperature(0.4)
                .build();
    }

    /**
     * Creates and configures the OpenAI chat model with API credentials.
     *
     * @param apiKey API key for OpenAI authentication
     * @param options Configured chat options
     * @return ChatModel instance for OpenAI interaction
     */
    @Bean
    @Qualifier(value = "OpenAi")
    public ChatModel setChatModel(@Value("${spring.ai.openai.api-key}") String apiKey, OpenAiChatOptions options) {
        OpenAiApi api = new OpenAiApi("https://api.openai.com", apiKey);
        return new OpenAiChatModel(api, options);
    }
}