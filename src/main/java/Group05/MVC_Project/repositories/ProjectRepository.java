package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = "SELECT id,project_code,project_name,id_status,id_type FROM project WHERE id_status = 1",
            nativeQuery = true )
    List<Object> ListProjectActive();

    @Query(value = "SELECT id,project_code,project_name,id_status,id_type FROM project WHERE id_status = 2",
            nativeQuery = true )
    List<Object> ListProjectInActive();
}
