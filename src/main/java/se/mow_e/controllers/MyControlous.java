package se.mow_e.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyControlous {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World";
    }

}
