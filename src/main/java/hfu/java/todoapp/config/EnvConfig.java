package hfu.java.todoapp.config;

import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class EnvConfig {
    
    @Autowired
    private Environment env;

    @PostConstruct
    public void validate() {
        validateEnvVariable("POSTGRES_URL");
        validateEnvVariable("POSTGRES_USERNAME");
        validateEnvVariable("POSTGRES_PASSWORD");
    }

    private void validateEnvVariable(String name) {
        String value = env.getProperty(name);
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Environment variable " + name + " is not set");
        }
    }
} 