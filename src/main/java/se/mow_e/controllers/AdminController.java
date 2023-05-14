package se.mow_e.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.mow_e.models.LoginRequest;
import se.mow_e.repository.CoordinateRepo;
import se.mow_e.repository.MowerRepo;
import se.mow_e.services.AuthService;

import java.util.List;

@RestController
@SecurityRequirement(name = "auth")
@RequestMapping("/api")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private AuthService authService;

    @Autowired
    private MowerRepo mowerRepo;

    @Autowired
    private CoordinateRepo coordinateRepo;


    //----------------------Admin

    @GetMapping(value = "/dashboard/admin/login")
    public boolean signInAdmin(@RequestBody LoginRequest loginRequest) {                // SignIn for Admins
        // TODO
        return true;
    }

    @PostMapping(value = "/dashboard/admin/signup")
    public ResponseEntity<?> registerAdmin(@RequestBody LoginRequest loginRequest) {             // SignUp for Admins
        // TODO
        return ResponseEntity.ok().build();
    }

    //----------------------User

    @GetMapping(value = "/dashboard/users/countOwners")
    public Integer getActiveUsersCount() {      // Amount of users that own a mower
        // TODO
        return 1;
    }

    @GetMapping(value = "/dashboard/users/ownersList")
    public List<?> getActiveUsersList() {       // List of users that own a mower
        // TODO
        return List.of();
    }

    @GetMapping(value = "/dashboard/users/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {               // Delete a user
        // TODO
        return ResponseEntity.ok().build();
    }

    //----------------------Mower

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

    //----------------------Extra

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
