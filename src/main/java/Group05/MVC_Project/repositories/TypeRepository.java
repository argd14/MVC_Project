package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {
    @Query(value = "SELECT id,type FROM type ",
            nativeQuery = true )
    List<Object> ListTypes();
}
