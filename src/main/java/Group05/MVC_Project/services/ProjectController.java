package Group05.MVC_Project.services;

import Group05.MVC_Project.models.*;
import Group05.MVC_Project.repositories.ProjectRepository;
import Group05.MVC_Project.repositories.StatusRepository;
import Group05.MVC_Project.repositories.UserRepository;
import Group05.MVC_Project.utils.NumberValidation;
import Group05.MVC_Project.utils.SQLException;
import Group05.MVC_Project.utils.StringValidation;
import Group05.MVC_Project.utils.ValidateToken;
import antlr.collections.List;

import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;

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
                        if (stringValidation.validateAlphabetic(project.getDescription(), 250)) {
                            try {
                                Project newProject = projectRepository.save(project);
                                response.getDataset().add(newProject.getId());
                                response.setStatus(true);
                                response.setMessage("Saved successfully!");
                            } catch (DataAccessException ex) {
                                response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                            }
                        } else {
                            response.setException("Invalid description.");
                        }
                    } else {
                        response.setException("Invalid project name.");
                    }
                } else {
                    response.setException("Invalid project code.");
                }
            } else {
                response.setException("You are not a manager.");
            }
        }

        return response;
    }

    @GetMapping("/getAvailableDevelopers")
    public Response getAvailableDevelopers(@RequestHeader(value = "Authorization") String token){
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            try {
                response.getDataset().addAll(projectRepository.getAvailableDevelopers());
                response.setStatus(true);
            } catch (DataAccessException ex) {
                response.setException(SQLException.getException(String.valueOf(ex.getCause())));
            }
        }
        return response;
    }

    @PostMapping("/addDevelopersToProject")
    public Response addDevelopersToProject(@RequestHeader(value = "Authorization") String token, @RequestBody UserProject object){
        initializeResponse();
        if(!validateToken.validateToken(token)){
            response.setException("Unauthorized access.");
        } else {
            int rolDB = validateToken.userDB().getId_rol();
            if (rolDB != 3){
                if(!object.getDevelopers().isEmpty()){
                    try{
                        Project project = projectRepository.findById(object.getId_project()).get();

                        for (int i = 0; i < object.getDevelopers().size(); i++) {
                            User user = userRepository.findById(object.getDevelopers().get(i)).get();
                            if (!project.getUser().contains(user)){
                                project.getUser().add(user);
                                projectRepository.save(project);
                            }
                        }

                        response.setStatus(true);
                        response.setMessage("Saved successfully!");
                    } catch (DataAccessException ex){
                        response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                    }
                } else {
                    response.setException("You didn't add any developers to the project.");
                }
            } else {
                response.setException("You are not manager.");
            }
        }
        return response;
    }

    @DeleteMapping("/delete")
    public Response deleteProject(@RequestHeader(value = "Authorization") String token, @RequestParam(name = "id") Long id){
        initializeResponse();
        if(!validateToken.validateToken(token)){
            response.setException("Unauthorized access.");
        } else {
            int rolDB = validateToken.userDB().getId_rol();
            if (rolDB != 3){
                if (projectRepository.existsById(id)){
                    try {
                        projectRepository.deleteById(id);
                        response.setStatus(true);
                        response.setMessage("Project deleted successfully!");
                    } catch(DataAccessException ex) {
                        response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                    }
                } else {
                    response.setException("The id that you set does not exists.");
                }
            } else {
                response.setException("You are not manager.");
            }
        }
        return response;
    }

    

    @GetMapping("/getProjects")
    public Response getProjects(@RequestHeader(value = "Authorization") String token){
        initializeResponse();
        if (!validateToken.validateToken(token)){
            response.setException("Unauthorized access.");
        } else {
            int rolDB = validateToken.userDB().getId_rol();
            if (rolDB != 3){
                try{
                    response.getDataset().addAll(projectRepository.getProjects());
                    response.setStatus(true);
                }catch(StackOverflowError ex){
                    response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                }
            } else {
                response.setException("You are not manager.");
            }
        }
        return response;
    }

    @GetMapping("/search")
    public Response searchProjects(@RequestHeader(value = "Authorization") String token,
                                   @RequestParam(name = "search") String search){
        initializeResponse();
        if (!validateToken.validateToken(token)){
            response.setException("Unauthorized access.");
        } else {
            int rolDB = validateToken.userDB().getId_rol();
            if (rolDB != 3){
                try{
                    response.getDataset().addAll(projectRepository.searchProjects(search));
                    response.setStatus(true);
                }catch(StackOverflowError ex){
                    response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                }
            } else {
                response.setException("You are not manager.");
            }
        }
        return response;
    }

    public void initializeResponse() {
        this.response = new Response();
    }

}
