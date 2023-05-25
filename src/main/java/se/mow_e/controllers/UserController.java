package se.mow_e.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.mow_e.models.Coordinate;
import se.mow_e.models.Mower;
import se.mow_e.repository.CoordinateRepo;
import se.mow_e.repository.MowerRepo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "auth")
@RequestMapping("/api")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @Autowired
    private MowerRepo mowerRepo;

    @Autowired
    private CoordinateRepo coordinateRepo;

    @GetMapping(value = "/mower/history")
    public List<Coordinate> getHistory(
            @RequestParam(required = false) boolean allSessions,
            @RequestParam(required = false) Integer sessionId,
            Principal principal) {

        List<Mower> mowers = mowerRepo.findAllByUsername(principal.getName());

        if (!mowers.isEmpty()) {    // Костыль ебаный - BAND-AID kill me for this
            String mowerId = mowers.get(0).getMowerId();
            if (sessionId != null) {
                return coordinateRepo.findAllBySessionIdAndMowerId(Long.valueOf(sessionId), mowerId);
            } else if (!allSessions) {
                Long lastSessionId = coordinateRepo.findLastSessionId(mowerId).orElse(null);
                return coordinateRepo.findAllBySessionIdAndMowerId(lastSessionId, mowerId);
            } else {
                return coordinateRepo.findCoordinatesByMowerId(mowerId);
            }
        } else {
            return List.of();
        }
    }

    @GetMapping(value = "/mower/images/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable String imageId, Principal principal) throws IOException {

        Mower mower = mowerRepo.findMowerByImagesContains(imageId);

        if (mower != null && principal.getName().equals(mower.getUsername())) {
            Path path = Paths.get("data/images/" + imageId + ".jpg");
            if (!Files.exists(path)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");

            ByteArrayResource inputStream = new ByteArrayResource(Files.readAllBytes(path));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentLength(inputStream.contentLength())
                    .body(inputStream);
        } else {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Image missing or not enough permissions");
        }
    }

    @PostMapping("/user/addMower/{mowerId}")  // Login for both users and admins
    public ResponseEntity<?> bindUserToMower(@PathVariable String mowerId, Principal principal) {

        Mower mower = mowerRepo.findMowerByMowerId(mowerId);

        if (mower != null) {
            if (principal.getName().equals(mower.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You already own zis robot");
            } else if (mower.getUsername() == null || mower.getUsername().isEmpty()) {
                mower.setUsername(principal.getName());
                mowerRepo.save(mower);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Map.of(
                                "status", "successful",
                                "message", "welcome to the club, buddy"
                        ));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This robot has another owner, welcome to friendzone");
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Your mower is fake trash, buy Husqvarna");
    }

}
