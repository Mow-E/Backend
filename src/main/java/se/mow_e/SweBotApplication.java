package se.mow_e;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@OpenAPIDefinition(info = @Info(title = "SweBot", version = "Sigma"))
@SpringBootApplication
@EnableScheduling
public class SweBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SweBotApplication.class, args);
    }

}