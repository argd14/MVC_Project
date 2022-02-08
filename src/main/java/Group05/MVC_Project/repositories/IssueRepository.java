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

    @Query(value = "SELECT id,summary,created_by,id_priority,id_project,id_type FROM issue",
            nativeQuery = true )
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

}

