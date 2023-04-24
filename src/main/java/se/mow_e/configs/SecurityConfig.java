package se.mow_e.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.mow_e.components.EncoderComponent;
import se.mow_e.components.JwtRequestFilter;
import se.mow_e.services.JwtService;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.OutputStream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private EncoderComponent encoder;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/login").anonymous()
                .antMatchers("/home").hasAuthority("user")
                .antMatchers("/h2-console", "/h2-console/**", "/websocket", "/coordinate").permitAll()
                .antMatchers("/swagger-ui", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                .antMatchers("/auth/*").permitAll()
                .anyRequest().authenticated()
            .and()
                .logout().permitAll().logoutSuccessUrl("/login").invalidateHttpSession(true)
            .and()
                .exceptionHandling()
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        sendError(response, HttpServletResponse.SC_FORBIDDEN);
                    })
                    .authenticationEntryPoint((request, response, authException) -> {
                        sendError(response, HttpServletResponse.SC_UNAUTHORIZED);
                    });

        http.headers()
                .frameOptions()
                .sameOrigin();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private static void sendError(HttpServletResponse response, int statusCode) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(statusCode);
        OutputStream responseStream = response.getOutputStream();
        responseStream.write(("{\"status\": \"error\", \"message\": \""+HttpStatus.resolve(statusCode).getReasonPhrase()+"\"}").getBytes());
        responseStream.flush();
    }

    @Bean
    UserDetailsManager users(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        UserDetails user = User.builder()
                .username("user")
                .password(encoder.passwordEncoder().encode("pass"))
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.passwordEncoder().encode("pass"))
                .roles("USER", "ADMIN")
                .build();

        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setJdbcTemplate(jdbcTemplate);

        if(!manager.userExists(user.getUsername())) manager.createUser(user);
        if(!manager.userExists(admin.getUsername())) manager.createUser(admin);

        return manager;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public JwtService jwtUtil() {
        return new JwtService();
    }

}
