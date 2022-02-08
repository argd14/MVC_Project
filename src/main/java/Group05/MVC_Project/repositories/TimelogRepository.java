package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.Dates;
import Group05.MVC_Project.models.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimelogRepository extends JpaRepository<TimeLog, Long> {

    @Query(value = "SELECT hours, comment, id_user, id_status, creation_date FROM time_log WHERE id_issue = :id",
            nativeQuery = true )
    List<Object> listDatesByIssue(@Param("id_issue") Long id);

}