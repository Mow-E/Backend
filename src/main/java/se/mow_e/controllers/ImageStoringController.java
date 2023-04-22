package se.mow_e.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import se.mow_e.models.ImageRequest;
import se.mow_e.models.LoginRequest;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RestController
@RequestMapping("/images")
public class ImageStoringController {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @GetMapping("/")
    public String setupDatabase() {
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connected to the h2 database");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @PostMapping("/addimage")
    public String addImage(@RequestBody ImageRequest imageRequest) {
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            String sql = "INSERT INTO images (name, description) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, imageRequest.getName());
                statement.setString(2, imageRequest.getDescription());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return "error";
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }
    @GetMapping("/getimage")
    public String getImage(@RequestBody ImageRequest imageRequest) {
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            String sql = "INSERT INTO images (name, description) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, imageRequest.getName());
                statement.setString(2, imageRequest.getDescription());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return "error";
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }
}
