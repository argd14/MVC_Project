package Group05.MVC_Project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevelopmentCycleRepository extends JpaRepository<DevelopmentCicle, Long> {


    @Query(value = "SELECT c.id,c.cycle_name,c.duration,c.start_date,c.end_date,c.description,s.status FROM development_cycle c, status s WHERE  c.id_status = s.id",
            nativeQuery = true)
    List<Object> ListDevelopmentCycle();


    @Query(value = "SELECT id,cycle_name,duration,start_date,end_date,description FROM `development_cycle` WHERE id = :id ",
            nativeQuery = true)
    Object developmentCycle(@Param("id") Long id);
}
