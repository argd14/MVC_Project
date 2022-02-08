package Group05.MVC_Project.models;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Data
@Table(name = "issue")
public class Issue implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String summary;
    private String description;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User created_by;
    @ManyToOne
    @JoinColumn(name = "issue_owner")
    private User issue_owner;
    @ManyToOne
    @JoinColumn(name = "id_priority")
    private Priority id_priority;
    @ManyToOne
    @JoinColumn(name = "id_project")
    private Project id_project;
    @ManyToOne
    @JoinColumn(name = "id_status")
    private Status id_status;
    @ManyToOne
    @JoinColumn(name = "id_type")
    private Type id_type;
    @ManyToOne
    @JoinColumn(name = "id_score")
    private Score id_score;
    @ManyToOne
    @JoinColumn(name = "id_development_cycle")
    private DevelopmentCicle id_development_cycle;
    private LocalDateTime creation_date;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_issue",
            joinColumns = @JoinColumn(name = "id_issue"),
            inverseJoinColumns = @JoinColumn(name = "id_user"))
    private Collection<User> user;
}


