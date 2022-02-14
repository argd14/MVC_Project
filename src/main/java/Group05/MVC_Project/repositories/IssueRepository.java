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

    @Query(value = "SELECT i.id, i.summary, p.priority, t.type, s.status, u1.name as created_by, u2.name as issue_owner, pr.project_name FROM issue i "+
                "INNER JOIN priority p ON i.id_priority = p.id " +
                "INNER JOIN type t ON i.id_type = t.id " +
                "INNER JOIN status s ON i.id_status = s.id " +
                "INNER JOIN user u1 ON i.created_by = u1.id_user " +
                "LEFT JOIN user u2 ON i.issue_owner = u2.id_user " +
                "INNER JOIN project pr ON i.id_project = pr.id_project",
        nativeQuery = true)
    List<Object> getIssues();

    @Query(value = "SELECT id,summary,created_by,id_priority,id_project,id_type FROM issue WHERE summary LIKE %:search% ",
            nativeQuery = true )
    List<Object> searchIssues(@Param("search") String search);

    @Query(value = "SELECT * FROM issue WHERE id_issue = :id",
            nativeQuery = true )
    Object getIssueDetails(@Param("id") Long id);

    @Query(value = "SELECT u.id_user, u.name FROM user_issue i, user u WHERE i.id_user = u.id_user AND i.id_issue = :id",
            nativeQuery = true )
    List<Object> getDevelopersPerIssue(@Param("id") Long id);

    @Query(value = "SELECT c.id,c.summary FROM issue c WHERE c.id_development_cycle = 1",
            nativeQuery = true)
    List<Object> getAvailableIssues();
    @Query(value = "SELECT * FROM issue WHERE created_by = :id",
            nativeQuery = true)
    List<Issue> issuesByIdUser(@Param("id") Long id);

    //List<Issue> findAll()

    @Query(value = "SELECT c.id,c.summary FROM issue c WHERE c.id_development_cycle = :id",
            nativeQuery = true)
    List<Object> getAvailableIssuesBySprint(@Param("id") Long id);

    /*
     *
     *
     * QUERIES FOR DEVELOPERS SITE
     *
     *
     */

    @Query(value = "SELECT i.id, i.summary, p.priority, t.type, s.status, u1.name as created_by, u2.name as issue_owner, i.description FROM issue i "+
            "INNER JOIN priority p ON i.id_priority = p.id " +
            "INNER JOIN type t ON i.id_type = t.id " +
            "INNER JOIN status s ON i.id_status = s.id " +
            "INNER JOIN user u1 ON i.created_by = u1.id_user " +
            "INNER JOIN user u2 ON i.issue_owner = u2.id_user " +
            "WHERE i.id = :id",
            nativeQuery = true)
    Object getTaskInfo(@Param("id") Long id);

}
