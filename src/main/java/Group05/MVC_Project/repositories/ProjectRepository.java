package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.Project;
import Group05.MVC_Project.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = "SELECT id_project,project_code,project_name FROM project",
            nativeQuery = true )
    List<Object> getProjects();

    @Query(value = "SELECT id_project,project_code,project_name FROM project WHERE project_code LIKE %:search% OR project_name LIKE %:search%",
           nativeQuery = true )
    List<Object> searchProjects(@Param("search") String search);

    @Query(value = "SELECT id_project,project_code,project_name,description FROM project WHERE id_project = :id",
            nativeQuery = true )
    Object getProjectDetails(@Param("id") Long id);

    @Query(value = "SELECT u.id_user, u.name FROM user_project p, user u WHERE p.id_user = u.id_user AND p.id_project = :id",
            nativeQuery = true )
    List<Object> getDevelopersOfAProject(@Param("id") Long id);

    @Query(value = "SELECT id_user, name FROM user WHERE id_rol = 3 AND id_status = 1;",
            nativeQuery = true )
    List<Object> getAvailableDevelopers();
}
