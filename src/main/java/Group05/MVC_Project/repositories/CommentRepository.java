package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT id,comment,id_user FROM comment WHERE id_status = 1",
            nativeQuery = true )
    List<Object> ListComment();

}
