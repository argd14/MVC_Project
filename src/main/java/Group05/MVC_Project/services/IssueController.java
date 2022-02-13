package Group05.MVC_Project.services;

import Group05.MVC_Project.models.Issue;
import Group05.MVC_Project.models.Response;
import Group05.MVC_Project.repositories.*;
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
    private ValidateToken validateToken;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private PriorityRepository priorityRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private UserRepository userRepository;

    private Response response;
    private StringValidation stringValidation = new StringValidation();
    private NumberValidation numberValidation = new NumberValidation();

    @PostMapping("/create")
    public Response createIssue(@RequestHeader(value = "Authorization") String token, @RequestBody Issue issue) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (stringValidation.validateAlphanumeric(issue.getSummary(), 150)) {
                if (stringValidation.validateAlphanumeric(issue.getDescription(), 500)) {
                    if (numberValidation.validateInteger(String.valueOf(issue.getCreated_by()))) {
                        if (numberValidation.validateInteger(String.valueOf(issue.getId_priority()))) {
                            if (numberValidation.validateInteger(String.valueOf(issue.getId_project()))) {
                                if (numberValidation.validateInteger(String.valueOf(issue.getId_type()))) {
                                    try {
                                        issueRepository.save(issue);
                                        response.setStatus(true);
                                        response.setMessage("Saved successfully!");
                                    } catch (DataAccessException ex) {
                                        response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                                    }
                                } else {
                                    response.setException("invalid type");
                                }
                            } else {
                                response.setException("invalid project");
                            }
                        } else {
                            response.setException("invalid priority");
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

        }
        return response;
    }

    @PostMapping("/update")
    public Response updateIssue(@RequestHeader(value = "Authorization") String token, @RequestBody Issue issue) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (issue.getId() != null) {
                if (stringValidation.validateAlphanumeric(issue.getSummary(), 150)) {
                    if (stringValidation.validateAlphanumeric(issue.getDescription(), 500)) {
                            if (numberValidation.validateInteger(String.valueOf(issue.getId_priority()))) {
                                if (numberValidation.validateInteger(String.valueOf(issue.getId_project()))) {
                                    if (numberValidation.validateInteger(String.valueOf(issue.getId_type()))) {
                                        try {
                                            Issue issueDB = issueRepository.findById(issue.getId()).get();
                                            issueDB.setSummary(issue.getSummary());
                                            issueDB.setDescription(issue.getDescription());
                                            issueDB.setId_priority(issue.getId_priority());
                                            issueDB.setId_project(issue.getId_project());
                                            issueDB.setId_status(issue.getId_status());
                                            issueDB.setId_type(issue.getId_type());
                                            issueDB.setId_score(issue.getId_score());
                                            issueDB.setId_development_cycle(issue.getId_development_cycle());
                                            issueRepository.save(issueDB);
                                            response.setMessage("Updated successfully.");
                                            response.setStatus(true);
                                        } catch (DataAccessException ex) {
                                            response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                                        }
                                    } else {
                                        response.setException("invalid type");
                                    }
                                } else {
                                    response.setException("invalid project");
                                }
                            } else {
                                response.setException("invalid priority");
                            }
                    } else {
                        response.setException("invalid description");
                    }
                } else {
                    response.setException("invalid summary");
                }
            } else {
                response.setException("id can not be null");
            }
        }
        return response;
    }

        @GetMapping("/issues")
        public Response getAvailableDevelopers(@RequestHeader(value = "Authorization") String token){
            initializeResponse();
            if (!validateToken.validateToken(token)) {
                response.setException("Unauthorized access.");
            } else {
                try {
                    response.getDataset().addAll(issueRepository.getIssues());
                    response.setStatus(true);
                } catch (DataAccessException ex) {
                    response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                }
            }
            return response;
        }

    @GetMapping("/issue")
    public Response getIssue(@RequestHeader(value = "Authorization") String token, @RequestParam(name = "id") Long id) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (issueRepository.findById(id) != null) {
                response.getDataset().add(issueRepository.findById(id).get());
                response.setStatus(true);
            } else {
                response.setException("The issue doesn't exists.");
            }
        }
        return response;
    }

    @DeleteMapping("/delete")
    public Response deleteIssue(@RequestHeader(value = "Authorization") String token, @RequestParam(name = "id") Long id) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (issueRepository.findById(id).get() != null) {
                try {
                    issueRepository.deleteById(id);
                    response.setStatus(true);
                    response.setMessage("Issue deleted successfully!");
                } catch (DataAccessException ex) {
                    response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                }
            } else {
                response.setException("The issue doesn't exists.");
            }
        }
        return response;
    }

    @GetMapping("/priorities")
    public Response getPriorities(@RequestHeader(value = "Authorization") String token){
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            try {
                response.getDataset().addAll(priorityRepository.ListPriority());
                response.setStatus(true);
            } catch (DataAccessException ex) {
                response.setException(SQLException.getException(String.valueOf(ex.getCause())));
            }
        }
        return response;
    }

    @GetMapping("/types")
    public Response getTypes(@RequestHeader(value = "Authorization") String token){
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            try {
                response.getDataset().addAll(typeRepository.ListTypes());
                response.setStatus(true);
            } catch (DataAccessException ex) {
                response.setException(SQLException.getException(String.valueOf(ex.getCause())));
            }
        }
        return response;
    }

    @GetMapping("/scores")
    public Response getScores(@RequestHeader(value = "Authorization") String token){
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            try {
                response.getDataset().addAll(scoreRepository.ListScore());
                response.setStatus(true);
            } catch (DataAccessException ex) {
                response.setException(SQLException.getException(String.valueOf(ex.getCause())));
            }
        }
        return response;
    }


    @GetMapping("/ListIssueStatus")
    public Response getIssueListStatus(@RequestHeader(value = "Authorization") String token) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            try {
                response.getDataset().addAll(statusRepository.ListIssueStatus());
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
