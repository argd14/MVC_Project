package Group05.MVC_Project.models;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @Column(name = "id_user", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String userName;
    private String phoneNumber;
    private String email;
    private String password;
    private int rol;
}
