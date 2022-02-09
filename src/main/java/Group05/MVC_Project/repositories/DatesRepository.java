package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.Dates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatesRepository extends JpaRepository<Dates, Long> {

    @Query(value = "SELECT started_date,end_date,target_date FROM dates WHERE id_issue = :id",
            nativeQuery = true )
    List<Object> listDatesByIssue(@Param("id_issue") Long id);

}
