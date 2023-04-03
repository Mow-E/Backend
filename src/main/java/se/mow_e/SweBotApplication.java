package se.mow_e;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "SweBot", version = "Sigma"))
@SpringBootApplication
public class SweBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SweBotApplication.class, args);
    }

}