package se.mow_e.controllers;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.provisioning.UserDetailsManager;
import se.mow_e.services.JwtService;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:test")
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    RequestTokenConfig requestTokenConfig;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsManager detailsManager;

    @Test
    public void loginEndpointShouldReturnAToken() throws Exception {
        String response = requestTokenConfig.authRequest("admin", "pass",
                HttpMethod.POST, "/auth/login", HttpStatus.OK.value());

        Assertions.assertEquals(JsonPath.read(response, "$.status"), "successful");

        Authentication authentication = jwtService.getAuthentication(requestTokenConfig.getToken(response));

        Assertions.assertTrue(authentication.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority())));
    }

    @Test
    void signUpEndpointDoNotRegisterExistingUsername() throws Exception {

        String response = requestTokenConfig.authRequest("admin", "pass",
                HttpMethod.POST, "/auth/signup", HttpStatus.BAD_REQUEST.value());

        Assertions.assertEquals(JsonPath.read(response, "$.status"), "error");
    }

    @Test
    void signUpEndpointRegisterNewUser() throws Exception {

        String response = requestTokenConfig.authRequest("user3", "pass",
                HttpMethod.POST, "/auth/signup", HttpStatus.OK.value());

        Assertions.assertEquals(JsonPath.read(response, "$.status"), "successful");

        Assertions.assertTrue(detailsManager.userExists("user3"));
    }

}
