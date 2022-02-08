package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.Comment;
import Group05.MVC_Project.models.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT id,comment,id_user FROM comment WHERE id_issue=:id",
            nativeQuery = true )
    List<Object> listCommentByIssue(@Param("id") Long id);

}
