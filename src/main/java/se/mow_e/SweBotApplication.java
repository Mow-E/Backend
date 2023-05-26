package se.mow_e;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SuppressWarnings("SpringBootApplicationSetup")
@OpenAPIDefinition(info = @Info(title = "SweBot", version = "Sigma"), servers = {@Server(url = "/", description = "Default Server URL")})
@SecurityScheme(name = "auth", scheme = "bearer", bearerFormat = "jwt", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@EnableScheduling
@SpringBootApplication
@ComponentScan("se.mow_e")// <---- DO NOT TOUCH, Band-aid/Feature for Tests
public class SweBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SweBotApplication.class, args);
    }

}
