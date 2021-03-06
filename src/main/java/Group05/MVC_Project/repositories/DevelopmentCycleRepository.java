package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.DevelopmentCicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface DevelopmentCycleRepository extends JpaRepository<DevelopmentCicle, Long> {


    @Query(value = "SELECT c.id,c.cycle_name,c.duration,c.start_date,c.end_date,c.description,s.status FROM development_cycle c, status s WHERE  c.id_status = s.id AND c.id_status = 1",
            nativeQuery = true)
    List<Object> ListDevelopmentCycle();


    @Query(value = "SELECT id,cycle_name,duration,start_date,end_date,description FROM `development_cycle` WHERE id = :id ",
            nativeQuery = true)
    Object developmentCycle(@Param("id") Long id);

    @Query(value = "SELECT u.name, COUNT(i.id) AS fixes FROM issue i INNER JOIN user u ON i.issue_owner = u.id_user WHERE i.id_status = 5 GROUP BY u.name ORDER BY fixes DESC LIMIT 6",
        nativeQuery = true)
    List<Object> getDoneTasks();

    @Query(value="SELECT s.status, (COUNT(i.id)*100 / (SELECT COUNT(id) FROM issue WHERE id_project = :id)) as percentage, p.project_name FROM issue i INNER JOIN status s ON i.id_status = s.id INNER JOIN project p ON i.id_project = p.id_project WHERE i.id_project = :id GROUP BY s.status",
        nativeQuery = true)
    List<Object> getTasksByProject(@Param("id") Long id);

    @Query(value="SELECT p.id_project, p.project_name, COUNT(i.id) as tasks FROM issue i INNER JOIN project p ON i.id_project = p.id_project GROUP BY p.id_project, p.project_name",
        nativeQuery = true)
    List<Object> getProjectsWithTasks();    
    
    /*
    *
    *
    * QUERIES FOR DEVELOPER SITE
    *
    *
    */

    @Query(value = "SELECT i.id, i.summary, p.priority, t.type, s.status, u1.name as created_by, u2.name as issue_owner FROM issue i "+
    "INNER JOIN priority p ON i.id_priority = p.id " +
    "INNER JOIN type t ON i.id_type = t.id " +
    "INNER JOIN status s ON i.id_status = s.id " +
    "INNER JOIN user u1 ON i.created_by = u1.id_user " +
    "LEFT JOIN user u2 ON i.issue_owner = u2.id_user " +
    "WHERE id_development_cycle = :id_sprint AND id_project = :id_project",
    nativeQuery = true)
    ArrayList<Object> getTasksOfASprint(@Param("id_sprint") Long idDevelopmentCycle,
                                   @Param("id_project") Long idProject);

    @Query(value = "SELECT DISTINCT(id_development_cycle)FROM issue WHERE id_project = :id_project",
           nativeQuery = true)
    ArrayList<Long> getSprintsOfAProject(@Param("id_project") Long id_project);
}
