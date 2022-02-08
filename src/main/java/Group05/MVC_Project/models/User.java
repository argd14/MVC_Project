package Group05.MVC_Project.models;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;


@Data
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @Column(name = "id_user", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String userName;
    private String phone_number;
    private String email;
    private String password;
    private int id_status;
    private int id_rol;
    private LocalDateTime creation_date;

    @ManyToMany(mappedBy = "user")
    private Collection<Project> project;
}
