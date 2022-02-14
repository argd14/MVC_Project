package Group05.MVC_Project.models;

import lombok.Data;

import java.util.ArrayList;

@Data
public class SprintIssue {
    private Long id;
    private ArrayList<Long> issues = new ArrayList<>();
}
