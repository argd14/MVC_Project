package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriorityRepository extends JpaRepository<Priority,Long> {
    @Query(value = "SELECT id,priority FROM priority",
            nativeQuery = true )
    List<Object> ListPriority();
}
