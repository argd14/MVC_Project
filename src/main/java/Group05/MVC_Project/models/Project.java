package Group05.MVC_Project.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Data
@Table(name = "project")
@NoArgsConstructor
public class Project implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_project", nullable = false)
    private Long id;

    private String project_code;
    private String project_name;

    @Transient
    private Long stat;
    @ManyToOne
    @JoinColumn(name = "id_status")
    private Status id_status ;

    private String description;

    private LocalDateTime creation_date;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_project",
            joinColumns = @JoinColumn(name = "id_project"),
            inverseJoinColumns = @JoinColumn(name = "id_user"))
    private Collection<User> user;


}
