package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.DevelopmentCicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevelopmentCycleRepository extends JpaRepository<DevelopmentCicle, Long> {

    @Query(value = "SELECT id,cycle_name FROM development_cycle WHERE id_status = 1",
            nativeQuery = true )
    List<Object> ListDevelopmentCycle();
}
