package Group05.MVC_Project.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "project")
@NoArgsConstructor
public class Project implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
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


}
