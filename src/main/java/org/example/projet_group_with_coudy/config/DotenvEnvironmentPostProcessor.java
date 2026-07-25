package org.example.projet_group_with_coudy.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Charge le fichier {@code .env} (a la racine du projet, jamais commite) et
 * expose son contenu comme proprietes Spring, avec la priorite la plus haute
 * (avant application.properties). Remplace spring-dotenv, non compatible
 * avec cette version de Spring Boot.
 */
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String DOTENV_FILE_NAME = ".env";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Path dotenvPath = Path.of(DOTENV_FILE_NAME);
        if (!Files.isRegularFile(dotenvPath)) {
            return;
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(dotenvPath)) {
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de lire le fichier " + DOTENV_FILE_NAME, e);
        }

        environment.getPropertySources().addFirst(new PropertiesPropertySource("dotenv", properties));
    }
}
