package Group05.MVC_Project.services;

import Group05.MVC_Project.models.Project;
import Group05.MVC_Project.models.Response;
import Group05.MVC_Project.models.Status;
import Group05.MVC_Project.models.User;
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
@RequestMapping("api/projects")
public class ProjectController {

    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ValidateToken validateToken;
    @Autowired
    private ProjectRepository projectRepository;

    private Response response;
    private StringValidation stringValidation = new StringValidation();
    private NumberValidation numberValidation = new NumberValidation();

    @PostMapping("/create")
    public Response createProject(@RequestHeader(value = "Authorization") String token, @RequestBody Project project) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            User userDB = validateToken.userDB();
            if (userDB.getId_rol() != 3) {
                if (stringValidation.validateAlphanumeric(project.getProject_code(), 15)) {
                    if (stringValidation.validateAlphabetic(project.getProject_name(), 150)) {
                        if (numberValidation.validateInteger(String.valueOf(project.getStat()))) {
                            if (stringValidation.validateAlphabetic(project.getDescription(), 250)) {
                                if(projectRepository.getById(project.getId()).getProject_code() != project.getProject_code()){
                                    try {
                                        Status status = statusRepository.getById(project.getStat());
                                        project.setId_status(status);
                                        projectRepository.save(project);
                                        response.setStatus(true);
                                        response.setMessage("Saved successfully!");
                                    } catch (DataAccessException ex) {
                                        response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                                    }
                                }else{
                                    response.setException("project already exists");
                                }
                            } else {
                                response.setException("invalid description, only letters");
                            }
                        } else {
                            response.setException("invalid status");

                        }
                    } else {
                        response.setException("invalid project name");
                    }
                } else {
                    response.setException("invalid project code");

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
                        if (numberValidation.validateInteger(String.valueOf(project.getStat()))) {
                            if (stringValidation.validateAlphabetic(project.getDescription(), 250)) {
                                try {
                                    Status status = statusRepository.getById(project.getStat());
                                    Project projectDB = projectRepository.getById(project.getId());
                                    projectDB.setProject_code(project.getProject_code());
                                    projectDB.setProject_name(projectDB.getProject_name());
                                    projectDB.setDescription(project.getDescription());
                                    projectDB.setId_status(status);
                                    response.setMessage("Updated successfully.");
                                    response.setStatus(true);
                                }catch (DataAccessException ex) {
                                    response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                                }
                            } else {

                            }
                        } else {

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

    public void initializeResponse() {
        this.response = new Response();
    }

}
