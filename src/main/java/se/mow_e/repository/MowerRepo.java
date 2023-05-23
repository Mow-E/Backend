package se.mow_e.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.mow_e.models.Mower;

import java.util.List;

public interface MowerRepo extends JpaRepository<Mower, Long> {

    List<Mower> findAllByUsername(String username);

    Mower findMowerByImagesContains(String imageId);

    Mower findMowerByMowerId(String mowerId);

}
