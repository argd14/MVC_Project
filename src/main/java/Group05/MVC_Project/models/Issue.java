
package Group05.MVC_Project.models;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Data
@Table(name= "issue")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String summary;
    private String description;
    private int created_by;
    @Column(name = "issue_owner", insertable = false, nullable=true)
    private Integer issue_owner;
    private int id_priority;
    private int id_project;
    private int id_status;
    private int id_type;
    private int id_score;
    private int id_development_cycle;
    private LocalDate creation_date;


}