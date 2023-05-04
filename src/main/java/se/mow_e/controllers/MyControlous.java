package se.mow_e.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "auth")
public class MyControlous {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World";
    }

}
