package Group05.MVC_Project.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
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
    private String duration;
    private LocalDate start_date;
    private LocalDate end_date;
    private String description;

    private int id_status;

    private LocalDateTime creation_date;


}
