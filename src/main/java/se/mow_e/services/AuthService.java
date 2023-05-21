package se.mow_e.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.mow_e.components.EncoderComponent;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsManager manager;

    @Autowired
    private EncoderComponent encoder;


    public String createToken(String username, String password){

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Generate JWT token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return jwtService.generateToken(userDetails);
    }

    public String createUser(String username, String password, boolean isAdmin){

        if(manager.userExists(username)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exists");
        }
        User.UserBuilder user = User.builder()
                .username(username)
                .password(encoder.passwordEncoder().encode(password));

        if(isAdmin){
            user.roles("USER","ADMIN");
        } else {
            user.roles("USER");
        }

        UserDetails buildedUser = user.build();
        manager.createUser(buildedUser);

        return jwtService.generateToken(buildedUser);
    }

    public void deleteUser(String username) {
        manager.deleteUser(username);
    }

    public boolean userExists(String username) {
        return manager.userExists(username);
    }

    public UserDetails getUserByUsername(String username) throws UsernameNotFoundException {
        return manager.loadUserByUsername(username);
    }
}

