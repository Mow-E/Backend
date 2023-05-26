package se.mow_e.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.mow_e.models.Coordinate;
import se.mow_e.models.Mower;
import se.mow_e.repository.CoordinateRepo;
import se.mow_e.repository.MowerRepo;
import se.mow_e.utils.UtilImage;

import java.io.IOException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private MowerRepo mowerRepo;

    @Autowired
    private CoordinateRepo coordinateRepo;

    public List<Coordinate> getHistory(boolean allSessions, Integer sessionId, String username) {

        List<Mower> mowers = mowerRepo.findAllByUsername(username);

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

    public ByteArrayResource getImage(String imageId, String username) throws IOException {
        Mower mower = mowerRepo.findMowerByImagesContains(imageId);

        if (mower != null && username.equals(mower.getUsername())) {

            return UtilImage.getImage(imageId);
        } else {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Image missing or not enough permissions");
        }
    }

    public void bindUserToMower(String mowerId, String username) {

        Mower mower = mowerRepo.findMowerByMowerId(mowerId);

        if (mower != null) {
            if (username.equals(mower.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You already own zis robot");
            } else if (mower.getUsername() == null || mower.getUsername().isEmpty()) {
                mower.setUsername(username);
                mowerRepo.save(mower);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This robot has another owner, welcome to friendzone");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Your mower is fake trash, buy Husqvarna");
        }
    }

}
