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

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")  // Login for both users and admins
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        try {
            String token = authService.createToken(loginRequest.getUsername(), loginRequest.getPassword());

            // Return token in response
            return ResponseEntity.ok(Map.of(
                    "status", "successful",
                    "token", token
            ));

        } catch (BadCredentialsException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "message", "do not mess with me"
            ));
        }
    }

    @PostMapping("/signup") // Creates users
    public ResponseEntity<?> registerUser(@RequestBody LoginRequest signupRequest) {

        String token = authService.createUser(signupRequest.getUsername(), signupRequest.getPassword());

        return ResponseEntity.ok(Map.of(
                "status", "successful",
                "token", token
        ));
    }

}

