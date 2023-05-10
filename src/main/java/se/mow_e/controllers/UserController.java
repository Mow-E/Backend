package se.mow_e.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
import java.util.Objects;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class UserController {

    @Autowired
    private MowerRepo mowerRepo;

    @Autowired
    private CoordinateRepo coordinateRepo;

    @GetMapping(value = "/mower/history")
    public List<Coordinate> getHistory(Principal principal) {   // Костыль ебаный - BAND-AID kill me for this

        List<Mower> mowers = mowerRepo.findAllByUsername(principal.getName());

        if (!mowers.isEmpty()) {
            return coordinateRepo.findCoordinatesByMowerId(mowers.get(0).getMowerId());
        } else {
            return List.of();
        }

    }

    @GetMapping(value = "/mower/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable String id, Principal principal) throws IOException {

        Mower mower = mowerRepo.findMowerByImagesContains(id);
        if (mower != null && Objects.equals(mower.getUsername(), principal.getName())) {

            Path path = Paths.get("data/images/" + id + ".jpg");
            if (!Files.exists(path)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");

            ByteArrayResource inputStream = new ByteArrayResource(Files.readAllBytes(path));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentLength(inputStream.contentLength())
                    .body(inputStream);
        } else {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT,"Image missing or not enough permissions");
        }
    }

}
