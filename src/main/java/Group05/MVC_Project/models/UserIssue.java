package Group05.MVC_Project.models;

import lombok.Data;

import java.util.ArrayList;

@Data
public class UserIssue {
    private Long id_issue;
    private ArrayList<Long> issues = new ArrayList<>();
}
