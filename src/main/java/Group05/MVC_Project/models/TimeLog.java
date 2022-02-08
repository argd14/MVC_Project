package Group05.MVC_Project.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "dates")
public class TimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Double hours;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User id_user;

    @ManyToOne
    @JoinColumn(name = "id_issue")
    private Issue id_issue;

    @ManyToOne
    @JoinColumn(name = "id_status")
    private Status id_status;

    private LocalDateTime creation_date;
}
