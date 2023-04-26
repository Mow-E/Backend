package se.mow_e.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.mow_e.models.LoginRequest;
import se.mow_e.services.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            String token = authService.createToken(loginRequest.getUsername(), loginRequest.getPassword());

            response.put("status", "successful");
            response.put("token", token);

            // Return token in response
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {

            response.put("status", "error");
            response.put("message", "do not mess with me"); // Wrong credentials, hashmap parser messes the order btw

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}

