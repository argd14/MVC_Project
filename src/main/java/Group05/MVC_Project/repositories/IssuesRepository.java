package Group05.MVC_Project.repositories;


import Group05.MVC_Project.models.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssuesRepository extends JpaRepository<Issue, Long> {

    @Query(value = "SELECT c.id,c.summary FROM issue c WHERE c.id_development_cycle = 1",
            nativeQuery = true)
    List<Object> getAvailableIssues();

    @Query(value = "SELECT c.id,c.summary FROM issue c WHERE c.id_development_cycle = :id",
            nativeQuery = true)
    List<Object> getAvailableIssuesBySprint(@Param("id") Long id);

}
