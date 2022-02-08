package Group05.MVC_Project.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "dates")
public class Dates implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private LocalDateTime started_date;

    private LocalDateTime end_date;

    private LocalDateTime target_date;

    @ManyToOne
    @JoinColumn(name = "id_issue")
    private Issue id_issue;

}
