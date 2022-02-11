package Group05.MVC_Project.services;

import Group05.MVC_Project.models.*;
import Group05.MVC_Project.repositories.*;
import Group05.MVC_Project.utils.NumberValidation;
import Group05.MVC_Project.utils.SQLException;
import Group05.MVC_Project.utils.StringValidation;
import Group05.MVC_Project.utils.ValidateToken;
import antlr.collections.List;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/manageSprints")
public class SprintController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DevelopmentCycleRepository developmentCycleRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private ValidateToken validateToken;

    private Response response;
    private StringValidation stringValidation = new StringValidation();
    private NumberValidation numberValidation = new NumberValidation();


    ////////////////////////////////////////////////////////////////
    @GetMapping("/sprints")
    public Response getSprints(@RequestHeader(value = "Authorization") String token) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");

        } else {
            response.getDataset().addAll(developmentCycleRepository.ListDevelopmentCycle());
            response.setStatus(true);
        }
        return response;
    }

    @GetMapping("/getAvailableIssues")
    public Response getAvailableIssues(@RequestHeader(value = "Authorization") String token) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            try {
                response.getDataset().addAll(issueRepository.getAvailableIssues());
                response.setStatus(true);
            } catch (DataAccessException ex) {
                response.setException(SQLException.getException(String.valueOf(ex.getCause())));
            }
        }
        return response;
    }

    @PostMapping("/addIssuesToSprint")
    public Response addIssuesToSprint(@RequestHeader(value = "Authorization") String token, @RequestBody SprintIssue object) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (!object.getIssues().isEmpty()) {
                try {
                    DevelopmentCicle developmentCicle = developmentCycleRepository.findById(object.getId()).get();
                    for (int i = 0; i < object.getIssues().size(); i++) {
                        Issue issue = issueRepository.findById(object.getIssues().get(i)).get();

                        if (issueRepository.existsById(issue.getId())) {
                            issue.setId_development_cycle(Math.toIntExact(developmentCicle.getId()));
                            issueRepository.save(issue);
                            response.setStatus(true);
                            response.setMessage("Saved successfully!");
                        }
                    }

                } catch (DataAccessException ex) {
                    response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                }
            } else {
                response.setException("You didn't add any developers to the project.");
            }
        }
        return response;
    }

    @GetMapping("/getAvailableIssuesBySprint")
    public Response getAvailableIssuesBySprint(@RequestHeader(value = "Authorization") String token, @RequestParam(name = "id") Long id) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            try {
                response.getDataset().addAll(issueRepository.getAvailableIssuesBySprint(id));
                response.setStatus(true);
            } catch (DataAccessException ex) {
                response.setException(SQLException.getException(String.valueOf(ex.getCause())));
            }
        }
        return response;
    }

    @DeleteMapping("/deleteSelectedById")
    public Response deleteSelectedById(@RequestHeader(value = "Authorization") String token, @RequestParam(name = "id") Long id) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (id != 0) {
                Issue issue = issueRepository.getById(id);
                issue.setId_development_cycle(1);
                issueRepository.save(issue);
                response.setStatus(true);
            }

        }

        return response;
    }

    @PostMapping("/createSprint")
    public Response createSprint(@RequestHeader(value = "Authorization") String token, @RequestBody DevelopmentCicle sprint) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            try {
                if (stringValidation.validateAlphanumeric(sprint.getCycle_name(), 40)) {
                    if (stringValidation.validateAlphanumeric(sprint.getDuration(), 40)) {
                        if (stringValidation.validateAlphabetic(sprint.getDescription(), 255)) {
                            developmentCycleRepository.save(sprint);
                            response.setStatus(true);
                            response.setMessage("Saved successfully!");
                        } else {
                            response.setException("invalid description");
                        }
                    } else {
                        response.setException("invalid duration");
                    }
                } else {
                    response.setException("invalid cycle name");
                }

            } catch (DataAccessException ex) {
                response.setException(SQLException.getException(String.valueOf(ex.getCause())));
            }

        }
        return response;
    }

    @GetMapping("/sprint")
    public Response getSprint(@RequestHeader(value = "Authorization") String token, @RequestParam(name = "id") Long id) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (developmentCycleRepository.findById(id) != null) {
                response.getDataset().add(developmentCycleRepository.findById(id).get());
                response.setStatus(true);
            } else {
                response.setException("The user doesn't exists.");
            }
        }
        return response;
    }

    @PostMapping("/updateSprint")
    public Response updateSprint(@RequestHeader(value = "Authorization") String token, @RequestBody DevelopmentCicle sprint) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (sprint.getId() != null) {
                if (stringValidation.validateAlphanumeric(sprint.getCycle_name(), 40)) {
                    if (stringValidation.validateAlphanumeric(sprint.getDuration(), 40)) {
                        if (stringValidation.validateAlphabetic(sprint.getDescription(), 255)) {
                            DevelopmentCicle sprintDB = developmentCycleRepository.findById(sprint.getId()).get();
                            sprintDB.setCycle_name(sprint.getCycle_name());
                            sprintDB.setDuration(sprint.getDuration());
                            sprintDB.setStart_date(sprint.getStart_date());
                            sprintDB.setEnd_date(sprint.getEnd_date());
                            sprintDB.setDescription(sprint.getDescription());
                            developmentCycleRepository.save(sprintDB);
                            response.setMessage("Updated successfully.");
                            response.setStatus(true);
                        } else {
                            response.setException("invalid description");

                        }
                    } else {
                        response.setException("invalid duration");

                    }
                } else {
                    response.setException("invalid cycle name");

                }

            } else {
                response.setException("id can not be null");
            }


        }


        return response;
    }

    @DeleteMapping("/deleteSprint")
    public Response deleteSprint(@RequestHeader(value = "Authorization") String token, @RequestParam(name = "id") Long id) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (developmentCycleRepository.findById(id).get() != null) {
                try {
                    developmentCycleRepository.deleteById(id);
                    response.setStatus(true);
                    response.setMessage("User deleted successfully!");
                } catch (DataAccessException ex) {
                    response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                }
            } else {
                response.setException("The user doesn't exists.");
            }
        }
        return response;
    }

    /*
    *
    *
    * ENDPOINTS FOR DEVELOPERS
    *
    *
    */

    public ArrayList<Long> getSprintsOfAProject(Long id_project){
        ArrayList<Long> sprintsList = new ArrayList<>();
        sprintsList = developmentCycleRepository.getSprintsOfAProject(id_project);
        return sprintsList;
    }

    @GetMapping("/getTasksOfASprint")
    public Response getTasksOfASprint (@RequestHeader(value = "Authorization", required = false) String token, @RequestParam(name = "id") Long id_project){
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access");
        } else {
            ArrayList<Long> sprintsList = new ArrayList<>();
            sprintsList = getSprintsOfAProject(id_project);
            if (!sprintsList.isEmpty()) {
                try {
                    for (Long aLong : sprintsList) {
                        ProjectSprint projectSprint = new ProjectSprint();
                        DevelopmentCicle sprint = developmentCycleRepository.findById(aLong).get();
                        projectSprint.setId_sprint(sprint.getId());
                        projectSprint.setSprint_name(sprint.getCycle_name());
                        projectSprint.setTasks(developmentCycleRepository.getTasksOfASprint(sprint.getId(), id_project));
                        response.getDataset().add(projectSprint);
                    }
                    response.setStatus(true);
                } catch (NullPointerException ex) {
                    response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                }
            } else {
                response.setException("This project does not have sprints.");
            }
        }
        return response;
    }


    public void initializeResponse() {
        this.response = new Response();
    }
    
}
