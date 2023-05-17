package se.mow_e.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.mow_e.models.LoginRequest;
import se.mow_e.models.Mower;
import se.mow_e.repository.CoordinateRepo;
import se.mow_e.repository.MowerRepo;
import se.mow_e.services.AuthService;
import se.mow_e.services.ImageService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@SecurityRequirement(name = "auth")
@RequestMapping("/api")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AuthService authService;

    @Autowired
    private MowerRepo mowerRepo;

    @Autowired
    private CoordinateRepo coordinateRepo;

    @Autowired
    private ImageService imageService;


    //----------------------Admin endpointus

    @PostMapping(value = "/dashboard/admin/signup")
    public ResponseEntity<?> registerAdmin(@RequestBody LoginRequest signupRequest) {             // SignUp for Admins

        String token = authService.createUser(signupRequest.getUsername(), signupRequest.getPassword(), true);

        return ResponseEntity.ok(Map.of(
                "status", "successful",
                "token", token
        ));

    }

    //----------------------User-data endpoints

    @GetMapping(value = "/dashboard/users/ownersList")
    public Set<?> getActiveUsersList() {       // List of users that own a mower

        return mowerRepo.findUsernames();
    }

    @GetMapping(value = "/dashboard/users/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {     // Delete a user

        if (authService.userExists(username)) {

            List<Mower> mowersList = mowerRepo.findAllByUsername(username);
            for (Mower mower : mowersList) {

                mower.setUsername(null);

                coordinateRepo.deleteAll(coordinateRepo.findCoordinatesByMowerId(mower.getMowerId()));
                mower.removeOldImages();
                authService.deleteUser(username);
            }
            mowerRepo.saveAll(mowersList);
        }
        return ResponseEntity.ok().build();
    }

    //----------------------Mower-data endpointus

    @GetMapping(value = "/dashboard/mowers/countOwned")
    public Integer getOwnedMowersCount() {     // Total amount of mowers owned by someone
        // TODO
        return 1;
    }

    @GetMapping(value = "/dashboard/mowers/ownedList")
    public List<String> getOwnedMowerList() {       // List of mowers that are owned by someone
        // TODO
        return List.of();
    }

    @GetMapping(value = "/dashboard/mowers/countActive")
    public Integer currentlyMowingMowers() {                 // Currently mowing mowers xD
        // TODO
        return 1;
    }

    @GetMapping(value = "/dashboard/mowers/totalHistorySessions")
    public Integer totalAmountOfMowingSessions() {         // Historical amount of mowing sessions of our robots
        // TODO
        return 1;
    }

    //----------------------Extra-functionality endpointus

    @GetMapping(value = "/dashboard/imageStorage/size")
    public Long imageStorageSize() {                         // Size of image storage in bytes
        // TODO
        return 5123123123L;
    }

    @PostMapping(value = "/dashboard/imageStorage/clean")
    public ResponseEntity<?> cleanImageStorage() {          // Call to free some space in image storage folder
        // TODO
        return ResponseEntity.ok().build();
    }

}
