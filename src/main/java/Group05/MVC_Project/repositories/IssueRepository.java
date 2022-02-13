package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.Issue;
import Group05.MVC_Project.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long>{

    @Query(value = "SELECT id,summary,created_by,id_priority,id_project,id_type FROM issue WHERE id_status = 1",
            nativeQuery = true )
    List<Object> ListOpenIssues();

    @Query(value = "SELECT id,summary,created_by,id_priority,id_project,id_type FROM issue WHERE id_status = 2",
            nativeQuery = true )
    List<Object> ListClosedIssues();

    @Query(value = "SELECT i.id,i.summary,u.name,p.priority,pro.project_name, t.type FROM issue i, user u, priority p, project pro, type t WHERE i.created_by = u.id_user AND i.id_priority = p.id AND i.id_project = pro.id_project AND i.id_type = t.id",
            nativeQuery = true )
    List<Object> getIssues();

    @Query(value = "SELECT id,summary,created_by,id_priority,id_project,id_type FROM issue WHERE summary LIKE %:search% ",
            nativeQuery = true )
    List<Object> searchIssues(@Param("search") String search);

    @Query(value = "SELECT u.id_user, u.name FROM user_issue i, user u WHERE i.id_user = u.id_user AND i.id_issue = :id",
            nativeQuery = true )
    List<Object> getDevelopersPerIssue(@Param("id") Long id);

    @Query(value = "SELECT c.id,c.summary FROM issue c WHERE c.id_development_cycle = 1",
            nativeQuery = true)
    List<Object> getAvailableIssues();

    @Query(value = "SELECT c.id,c.summary FROM issue c WHERE c.id_development_cycle = :id",
            nativeQuery = true)
    List<Object> getAvailableIssuesBySprint(@Param("id") Long id);
}

