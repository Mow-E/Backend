package se.mow_e;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@OpenAPIDefinition(info = @Info(title = "SweBot", version = "Sigma"))
@SpringBootApplication
@EnableScheduling
public class SweBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SweBotApplication.class, args);
    }

}