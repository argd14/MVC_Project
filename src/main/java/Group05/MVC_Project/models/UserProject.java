package Group05.MVC_Project.models;

import lombok.Data;

import java.util.ArrayList;

@Data
public class UserProject {
    private Long id_project;
    private ArrayList<Long> developers = new ArrayList<>();
}
