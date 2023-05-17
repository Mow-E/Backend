package se.mow_e.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.mow_e.models.Mower;

import java.util.List;
import java.util.Set;

public interface MowerRepo extends JpaRepository<Mower, Long> {

    List<Mower> findAllByUsername(String username);

    Mower findMowerByImagesContains(String imageId);

    Mower findMowerByMowerId(String mowerId);

    @Query("select m.username from Mower m where m.username != null and m.username != ''")
    Set<String> findUsernames();

}
