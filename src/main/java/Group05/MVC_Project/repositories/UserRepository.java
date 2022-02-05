package Group05.MVC_Project.repositories;

import Group05.MVC_Project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    
    @Query(value = "SELECT u FROM User u WHERE u.email LIKE %:email% ")
    User getCredentials(@Param("email") String email);

    @Query(value = "SELECT u.id_user, u.name, u.user_name, u.phone_number, u.email, s.status FROM user u, status s WHERE u.id_status = s.id AND u.id_rol = 3",
    nativeQuery = true)
    List<Object> developers();

    @Query(value = "SELECT u.id_user, u.name, u.user_name, u.phone_number, u.email, s.status FROM user u, status s WHERE u.id_status = s.id AND u.id_rol = 1",
    nativeQuery = true)
    List<Object> managers();
}
