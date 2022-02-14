package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    @Query(value = "SELECT id,status FROM status WHERE id = 1 OR id = 2",
            nativeQuery = true )
    List<Object> ListStatus();

    @Query(value = "SELECT id,status FROM status WHERE id = 3 OR id = 4 OR id = 5",
            nativeQuery = true )
    List<Object> ListIssueStatus();
}
