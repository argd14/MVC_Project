package Group05.MVC_Project.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "development_cycle")
public class DevelopmentCicle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String cycle_name;

    @ManyToOne
    @JoinColumn(name = "id_status")
    private Status id_status;

    private LocalDateTime creation_date;


}
