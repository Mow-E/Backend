package se.mow_e.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.mow_e.models.Coordinate;

import java.util.List;
import java.util.Optional;

public interface CoordinateRepo extends JpaRepository<Coordinate, Long> {
    // VOODOO magic moment (spring magic in action)
    @Query(value = "select session_id from coordinate_table where mower_id = :mowerId order by time desc limit 1",nativeQuery = true)   // Ignore this error
    Optional<Long> findLastSessionId(String mowerId);

    @Query(value = "select * from coordinate_table where extra = :tempImageId",nativeQuery = true)
    Coordinate findCoordinateByTempImageId(String tempImageId);

    @Query(value = "select * from coordinate_table where IMAGE_ID = :imageId",nativeQuery = true)
    Coordinate findCoordinateByImageId(String imageId);

    List<Coordinate> findCoordinatesByMowerId(String mowerId);

    List<Coordinate> findAllBySessionIdAndMowerId(Long sessionId, String mowerId);
}
