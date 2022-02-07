package Group05.MVC_Project.models;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "role")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String role;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    private LocalDateTime creation_date;
}
