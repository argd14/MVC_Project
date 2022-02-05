package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    @Query(value = "SELECT id,status FROM status ",
            nativeQuery = true )
    List<Object> ListStatus();
}
