package Group05.MVC_Project.services;

import Group05.MVC_Project.models.*;
import Group05.MVC_Project.repositories.IssueRepository;
import Group05.MVC_Project.repositories.ProjectRepository;
import Group05.MVC_Project.repositories.StatusRepository;
import Group05.MVC_Project.repositories.UserRepository;
import Group05.MVC_Project.utils.NumberValidation;
import Group05.MVC_Project.utils.SQLException;
import Group05.MVC_Project.utils.StringValidation;
import Group05.MVC_Project.utils.ValidateToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/issues")
public class IssueController {
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ValidateToken validateToken;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private IssueRepository issueRepository;

    private Response response;
    private StringValidation stringValidation = new StringValidation();
    private NumberValidation numberValidation = new NumberValidation();

    @PostMapping("/create")
    public Response createIssue(@RequestHeader(value = "Authorization") String token, @RequestBody Issue issue) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            User userDB = validateToken.userDB();
            if (userDB.getId_rol() != 3) {
                if (stringValidation.validateAlphanumeric(issue.getSummary(), 150)) {
                    if (stringValidation.validateAlphabetic(issue.getDescription(), 500)) {
                        if (numberValidation.validateInteger(String.valueOf(issue.getCreated_by()))) {
                            if (numberValidation.validateInteger(String.valueOf(issue.getId_status()))) {
                                if (numberValidation.validateInteger(String.valueOf(issue.getId_type()))) {
                                    if (numberValidation.validateInteger(String.valueOf(issue.getId_score()))) {
                                        if (numberValidation.validateInteger(String.valueOf(issue.getId_development_cycle()))) {
                                            try {
                                                issueRepository.save(issue);
                                                response.setStatus(true);
                                                response.setMessage("Saved successfully!");
                                            } catch (DataAccessException ex) {
                                                response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                                            }
                                        } else {
                                            response.setException("invalid developemt cycle");
                                        }
                                    } else {
                                        response.setException("invalid score");

                                    }
                                } else {
                                    response.setException("invalid type");
                                }
                            } else {
                                response.setException("invalid status");
                            }
                        } else {
                            response.setException("invalid user");
                        }
                    } else {
                        response.setException("invalid description");

                    }
                } else {
                    response.setException("invalid summary");
                }
            } else {
                response.setException("you are not a manager");
            }
        }

        return response;
    }

        @PostMapping("/update")
        public Response updateProject(@RequestHeader(value = "Authorization") String token, @RequestBody Project project) {
            initializeResponse();
            if (!validateToken.validateToken(token)) {
                response.setException("Unauthorized access.");
            } else {
                int rolDB = validateToken.userDB().getId_rol();
                if (rolDB != 3) {
                    if (stringValidation.validateAlphanumeric(project.getProject_code(), 15)) {
                        if (stringValidation.validateAlphabetic(project.getProject_name(), 150)) {
                                if (stringValidation.validateAlphabetic(project.getDescription(), 250)) {
                                    try {
                                        Project projectDB = projectRepository.getById(project.getId());
                                        projectDB.setProject_code(project.getProject_code());
                                        projectDB.setProject_name(projectDB.getProject_name());
                                        projectDB.setDescription(project.getDescription());
                                        response.setMessage("Updated successfully.");
                                        response.setStatus(true);
                                    }catch (DataAccessException ex) {
                                        response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                                    }
                                }
                            } else {

                            }
                        } else {

                        }
                    } else {

                    }
            }
            return response;
        }

        @GetMapping("/getAllIssues")
        public Response getAvailableDevelopers(@RequestHeader(value = "Authorization") String token){
            initializeResponse();
            if (!validateToken.validateToken(token)) {
                response.setException("Unauthorized access.");
            } else {
                try {
                    response.getDataset().addAll(issueRepository.findAll());
                    response.setStatus(true);
                } catch (DataAccessException ex) {
                    response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                }
            }
            return response;
        }

        public void initializeResponse() {
            this.response = new Response();
        }

    }
