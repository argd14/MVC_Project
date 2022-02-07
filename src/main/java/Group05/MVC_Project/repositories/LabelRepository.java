package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<Label,Long> {

    @Query(value = "SELECT id,label_name FROM label WHERE id_status = 1",
            nativeQuery = true )
    List<Object> ListLabel();
}
