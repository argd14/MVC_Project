package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score,Long> {

    @Query(value = "SELECT id,score FROM score ",
            nativeQuery = true )
    List<Object> ListScore();
}
