package se.mow_e.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping(path = {"/", "/login", "/dashboard", "/dashboard/**"})
    public String index() {
        return "index";
    }

}
