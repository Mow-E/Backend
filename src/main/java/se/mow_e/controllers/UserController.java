package se.mow_e.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.mow_e.models.Coordinate;
import se.mow_e.services.UserService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "auth")
@RequestMapping("/api")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/mower/history")
    public List<Coordinate> getHistory(
            @RequestParam(required = false) boolean allSessions,
            @RequestParam(required = false) Integer sessionId,
            Principal principal) {

        return userService.getHistory(allSessions, sessionId, principal.getName());
    }

    @GetMapping(value = "/mower/images/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable String imageId, Principal principal) throws IOException {

        ByteArrayResource imageData = userService.getImage(imageId, principal.getName());

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(imageData.contentLength())
                .body(imageData);
    }

    @PostMapping("/user/addMower/{mowerId}")  // Login for both users and admins
    public ResponseEntity<?> bindUserToMower(@PathVariable String mowerId, Principal principal) {

        userService.bindUserToMower(mowerId, principal.getName());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(
                        "status", "successful",
                        "message", "welcome to the club, buddy"
                ));
    }

}
