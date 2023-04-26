package se.mow_e.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;
import se.mow_e.services.ImageService;
import se.mow_e.websocket.messages.ImageMessage;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @MessageMapping("/images/add")
    public void add(ImageMessage message) {
        imageService.add(message);
    }

    @GetMapping("/images/{id}")
    public EntityResponse<?> get(@RequestParam String id) {
        return EntityResponse.fromObject("").build();
    }

}
