package se.mow_e.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.mow_e.models.Mower;

import java.util.List;
import java.util.Set;

public interface MowerRepo extends JpaRepository<Mower, Long> {

    @Query("select count(distinct m.username) from Mower m where m.username != null and m.username != ''")
    Integer queryCountOwners();

    @Query("select m.username from Mower m where m.username != null and m.username != ''")
    Set<String> queryOwners();

    List<Mower> findAllByUsername(String username);

    List<Mower> getAllMowersByUsernameIsNotNull();

    Mower findMowerByMowerId(String mowerId);

    Mower findMowerByImagesContains(String imageId);

    Integer countAllMowersByUsernameIsNotNull();

    Integer countAllMowersByStatusIsTrue();


}
