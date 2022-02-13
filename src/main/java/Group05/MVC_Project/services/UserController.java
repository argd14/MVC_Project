package Group05.MVC_Project.services;


import Group05.MVC_Project.models.*;
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
    private IssueRepository issueRepository;
    @Autowired
    private ValidateToken validateToken;
    @Autowired
    private ReportUtil service;

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
                                            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
                                            String hash = argon2.hash(1, 1024, 1, user.getPassword());
                                            user.setPassword(hash);
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

    @DeleteMapping("/delete")
    public Response deleteUser(@RequestHeader(value = "Authorization") String token, @RequestParam(name = "id") Long id) {
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
                    response.setException("Sorry! We can't delete this register because it's linked to a project.");
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
    public Response updatePassword(@RequestHeader(value = "Authorization") String token, @RequestParam(name = "password1") String password1, @RequestParam(name = "password2") String password2) {
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

