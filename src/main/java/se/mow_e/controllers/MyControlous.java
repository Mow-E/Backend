package se.mow_e.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class MyControlous {


    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World";
    }
}
