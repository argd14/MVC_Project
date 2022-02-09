package Group05.MVC_Project.services;


import Group05.MVC_Project.models.DevelopmentCicle;
import Group05.MVC_Project.models.Response;
import Group05.MVC_Project.models.User;
import Group05.MVC_Project.repositories.*;
import Group05.MVC_Project.utils.*;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DevelopmentCycleRepository developmentCycleRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private IssuesRepository issuesRepository;
    @Autowired
    private ValidateToken validateToken;

    private Response response;
    private StringValidation stringValidation = new StringValidation();
    private NumberValidation numberValidation = new NumberValidation();


    @PostMapping("/create")
    public Response createUser(@RequestHeader(value = "Authorization") String token, @RequestBody User user) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (stringValidation.validateAlphabetic(user.getName(), 40)) {
                if (stringValidation.validateAlphanumeric(user.getUserName(), 40)) {
                    if (numberValidation.validatePhone(user.getPhone_number())) {
                        if (stringValidation.validateEmail(user.getEmail())) {
                            if (stringValidation.validatePassword(user.getPassword())) {
                                if (numberValidation.validateInteger(String.valueOf(user.getId_rol()))) {
                                    if (numberValidation.validateInteger(String.valueOf(user.getId_status()))) {
                                        try {
                                            userRepository.save(user);
                                            response.setStatus(true);
                                            response.setMessage("Saved successfully!");
                                        } catch (DataAccessException ex) {
                                            response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                                        }
                                    } else {
                                        response.setException("Invalid status");
                                    }
                                } else {
                                    response.setException("Invalid rol");
                                }
                            } else {
                                response.setException("Invalid password. Please check that your password satisfies all the requirements.");
                            }
                        } else {
                            response.setException("Invalid email.");
                        }
                    } else {
                        response.setException("Invalid phone number.");
                    }
                } else {
                    response.setException("Invalid username.");
                }
            } else {
                response.setException("Invalid name.");
            }
        }
        return response;
    }

    @GetMapping("/user")
    public Response getUser(@RequestHeader(value = "Authorization") String token, @RequestParam(name = "id") Long id) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (userRepository.findById(id) != null) {
                response.getDataset().add(userRepository.findById(id).get());
                response.setStatus(true);
            } else {
                response.setException("The user doesn't exists.");
            }
        }
        return response;
    }

    @GetMapping("/users")
    public Response getUsers(@RequestHeader(value = "Authorization") String token) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
            response.setStatus(false);
        } else {
            response.getDataset().add(userRepository.findAll());
        }
        return response;
    }

    ///////////////////////////////////////
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
public Response getAvailableIssues(@RequestHeader(value = "Authorization") String token){
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            try{
                response.getDataset().addAll(issuesRepository.getAvailableIssues());
                response.setStatus(true);
            } catch(DataAccessException ex) {
                response.setException(SQLException.getException(String.valueOf(ex.getCause())));
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
    public Response deleteSprint(@RequestHeader(value = "Authorization") String
                                         token, @RequestParam(name = "id") Long id) {
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
////////////////////////////////////////////////////
    @DeleteMapping("/delete")
    public Response deleteUser(@RequestHeader(value = "Authorization") String
                                       token, @RequestParam(name = "id") Long id) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (userRepository.findById(id).get() != null) {
                try {
                    userRepository.deleteById(id);
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

    @PostMapping("/changeStatus")
    public Response changeStatus(@RequestHeader(value = "Authorization") String token, @RequestBody User user) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (user.getId() != null) {
                User userDB = userRepository.findById(user.getId()).get();
                if (userDB.getId_status() != user.getId_status()) {
                    try {
                        userDB.setId_status(user.getId_status());
                        userRepository.save(userDB);
                        response.setMessage("Status updated successfully.");
                        response.setStatus(true);
                    } catch (DataAccessException ex) {
                        response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                    }
                } else {
                    response.setException("You can't update a user to the same status.");
                }
            } else {
                response.setException("You can't do an update with empty fields.");
            }
        }
        return response;
    }

    @PostMapping("/update")
    public Response updateUser(@RequestHeader(value = "Authorization") String token, @RequestBody User user) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
                if (stringValidation.validateAlphabetic(user.getName(), 40)) {
                    if (stringValidation.validateAlphanumeric(user.getUserName(), 40)) {
                        if (numberValidation.validatePhone(user.getPhone_number())) {
                            if (stringValidation.validateEmail(user.getEmail())) {
                                try {
                                    User userDB = userRepository.findById(user.getId()).get();
                                    userDB.setName(user.getName());
                                    userDB.setUserName(user.getUserName());
                                    userDB.setPhone_number(user.getPhone_number());
                                    userDB.setEmail(user.getEmail());
                                    userDB.setId_rol(user.getId_rol());
                                    userRepository.save(userDB);
                                    response.setMessage("Updated successfully.");
                                    response.setStatus(true);
                                } catch (DataAccessException ex) {
                                    response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                                }
                            } else {
                                response.setException("Invalid email.");
                            }
                        } else {
                            response.setException("Invalid phone number.");
                        }
                    } else {
                        response.setException("Invalid username.");
                    }
                } else {
                    response.setException("Invalid name.");
                }
        }
        return response;
    }

    @GetMapping("/ListStatus")
    public Response getListStatus(@RequestHeader(value = "Authorization") String token) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            response.getDataset().addAll(statusRepository.ListStatus());
            response.setStatus(true);
        }
        return response;

    }

    @GetMapping("/ListRol")
    public Response getListRol(@RequestHeader(value = "Authorization") String token) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            response.getDataset().addAll(roleRepository.ListRole());
            response.setStatus(true);
        }
        return response;
    }

    @GetMapping("/getDevelopers")
    public Response getDevelopers(@RequestHeader(value = "Authorization") String token) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            response.getDataset().addAll(userRepository.developers(validateToken.userDB().getId()));
            response.setStatus(true);
        }
        return response;
    }

    @GetMapping("/getManagers")
    public Response getManagers(@RequestHeader(value = "Authorization") String token) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            response.getDataset().addAll(userRepository.managers(validateToken.userDB().getId()));
            response.setStatus(true);
        }
        return response;
    }

    @PostMapping("/updatePassword")
    public Response updatePassword(@RequestHeader(value = "Authorization") String
                                           token, @RequestParam(name = "password1") String password1, @RequestParam(name = "password2") String password2) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            User userDB = validateToken.userDB();
            if (stringValidation.validatePassword(password1)) {
                if (stringValidation.validatePassword(password2)) {
                    if (userDB.getPassword().equals(password1)) {
                        try {
                            userDB.setPassword(password2);
                            userRepository.save(userDB);
                            response.setStatus(true);
                            response.setMessage("Password updated successfully!");
                        } catch (DataAccessException ex) {
                            response.setException(SQLException.getException(String.valueOf(ex.getCause())));
                        }
                    } else {
                        response.setException("password not equals");
                    }
                } else {
                    response.setMessage("current password invalid");
                }
            } else {
                response.setMessage("new password invalid");
            }
        }
        return response;
    }

    public void initializeResponse() {
        this.response = new Response();
    }

}

