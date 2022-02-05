package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "SELECT id,role FROM role  WHERE status_id = 1",
            nativeQuery = true )
    List<Object> ListRole();
}
