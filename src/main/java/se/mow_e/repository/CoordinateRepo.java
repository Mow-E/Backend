package se.mow_e.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.mow_e.models.Coordinate;

import java.util.Optional;

public interface CoordinateRepo extends JpaRepository<Coordinate, Long> {
    // VOODOO magic moment (spring magic in action)
    @Query(value = "select session_id from coordinate_table where mower_id = :mowerId order by time desc limit 1",nativeQuery = true)   // Ignore this error
    public Optional<Long> findLastSessionId(String mowerId);

    @Query(value = "select * from coordinate_table where extra = :tempImageId",nativeQuery = true)
    public Coordinate findCoordinateByTempImageId(String tempImageId);
}
