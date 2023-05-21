package se.mow_e.services;


import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.mow_e.models.Mower;
import se.mow_e.repository.CoordinateRepo;
import se.mow_e.repository.MowerRepo;
import se.mow_e.utils.UtilImage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class AdminService {

    private final Logger log = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private MowerRepo mowerRepo;

    @Autowired
    private CoordinateRepo coordinateRepo;


    public Integer countOwners() {
        return mowerRepo.queryCountOwners();
    }


    public Set<String> getOwners() {
        return mowerRepo.queryOwners();
    }

    public void deleteUser(String username) {
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
    }


    public Integer countOwnedMowers() {
        return mowerRepo.countAllMowersByUsernameIsNotNull();
    }

    public List<Mower> getOwnedMowersList() {
        return mowerRepo.getAllMowersByUsernameIsNotNull();
    }


    public Integer countActiveMowers() {
        return mowerRepo.countAllMowersByStatusIsTrue();
    }


    public void cleanStorage() {
        try {
            FileUtils.cleanDirectory(new File(UtilImage.IMAGES_DIR));

        } catch (IOException e) {
            log.error("Anubis server error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Anestis did not clean ze folder ");
        }
    }

    public Long getStorageSize() {
        try {
            return UtilImage.getDirSize();
        } catch (IOException e) {

            log.error("Error while accessing the image folder: ", e);
            return -1L;
        }
    }
}
