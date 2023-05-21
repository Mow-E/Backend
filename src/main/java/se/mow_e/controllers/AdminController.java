package se.mow_e.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.mow_e.models.LoginRequest;
import se.mow_e.models.Mower;
import se.mow_e.services.AdminService;
import se.mow_e.services.AuthService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@SecurityRequirement(name = "auth")
@RequestMapping("/api")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AdminService adminService;

    // I do understand that these endpoints are trash/useless, but we need something to pimp the admin dashboard


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

    @GetMapping(value = "/dashboard/users/countOwners")
    public Integer getOwnersCount() {      // Amount of users that own a mower

        return adminService.countOwners();
    }

    @GetMapping(value = "/dashboard/users/ownersList")
    public Set<?> getOwnersList() {       // List of users that own a mower

        return adminService.getOwners();
    }

    @GetMapping(value = "/dashboard/users/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {     // Delete a user

        adminService.deleteUser(username);
        return ResponseEntity.ok().build();
    }


    //----------------------Mower-data endpointus

    @GetMapping(value = "/dashboard/mowers/countOwned")
    public Integer getOwnedMowersCount() {     // Total amount of mowers owned by someone

        return adminService.countOwnedMowers();
    }

    @GetMapping(value = "/dashboard/mowers/ownedList")
    public List<Mower> getOwnedMowerList() {       // List of mowers that are owned by someone

        return adminService.getOwnedMowersList();
    }

    @GetMapping(value = "/dashboard/mowers/countActive")
    public Integer currentlyMowingMowers() {    // Currently mowing mowers xD

        return adminService.countActiveMowers();
    }

    @GetMapping(value = "/dashboard/mowers/totalHistorySessions")
    public Integer totalAmountOfMowingSessions() {  // Historical amount of mowing sessions of our robots
        // TODO
        return 1337;
    }

    //----------------------Extra-functionality endpointus

    @GetMapping(value = "/dashboard/imageStorage/size")
    public Long imageStorageSize() {    // Size of image storage in bytes

       return adminService.getStorageSize();
    }

    @PostMapping(value = "/dashboard/imageStorage/clean")
    public ResponseEntity<?> cleanImageStorage() {          // Call to free some space in image storage folder

        adminService.cleanStorage();
        return ResponseEntity.ok().build();
    }

}
