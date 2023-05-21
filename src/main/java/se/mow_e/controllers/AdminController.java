package se.mow_e.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.mow_e.models.LoginRequest;
import se.mow_e.models.Mower;
import se.mow_e.repository.CoordinateRepo;
import se.mow_e.repository.MowerRepo;
import se.mow_e.services.AuthService;
import se.mow_e.services.ImageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@RestController
@SecurityRequirement(name = "auth")
@RequestMapping("/api")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {  // TODO REFACTOR ZIS SHIT INTO Services/Util

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

        return mowerRepo.queryCountOwners();
    }

    @GetMapping(value = "/dashboard/users/ownersList")
    public Set<?> getOwnersList() {       // List of users that own a mower

        return mowerRepo.queryOwners();
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

        return mowerRepo.countAllMowersByUsernameIsNotNull();
    }

    @GetMapping(value = "/dashboard/mowers/ownedList")
    public List<Mower> getOwnedMowerList() {       // List of mowers that are owned by someone

        return mowerRepo.getAllMowersByUsernameIsNotNull();
    }

    @GetMapping(value = "/dashboard/mowers/countActive")
    public Integer currentlyMowingMowers() {                 // Currently mowing mowers xD

        return mowerRepo.countAllMowersByStatusIsTrue();
    }

    @GetMapping(value = "/dashboard/mowers/totalHistorySessions")
    public Integer totalAmountOfMowingSessions() {         // Historical amount of mowing sessions of our robots
        // TODO
        return 1337;
    }

    //----------------------Extra-functionality endpointus

    @GetMapping(value = "/dashboard/imageStorage/size")
    public Long imageStorageSize() {                         // Size of image storage in bytes

        //  The try automatically closes the handler of walk after return (probably in finish, not sure)


        Path path = Paths.get("data/images/");

        if(!Files.exists(path)){
            return 0L;
        }
        try (Stream<Path> walk = Files.walk(path)) {
            return walk
                    .filter(p -> p.toFile().isFile())
                    .mapToLong(p -> p.toFile().length())
                    .sum();
        } catch (IOException e) {

            System.out.println("Error while accessing the image folder: " + e.getMessage());    // TODO normal log
            return -1L;
        }
    }

    @PostMapping(value = "/dashboard/imageStorage/clean")
    public ResponseEntity<?> cleanImageStorage() {          // Call to free some space in image storage folder


        try {
            FileUtils.cleanDirectory(new File("data/images/"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Anubis server error: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Anestis did not clean ze folder ");
        }
        return ResponseEntity.ok().build();
    }

}
