package Group05.MVC_Project.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class ProjectSprint {
    private Long id_sprint;
    private String sprint_name;
    private ArrayList<Object> tasks = new ArrayList<>();
}
