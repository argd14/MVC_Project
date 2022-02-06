package Group05.MVC_Project.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comment")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User id_user;

    @ManyToOne
    @JoinColumn(name = "id_status")
    private Status id_status;

    private LocalDateTime creation_date;

}
