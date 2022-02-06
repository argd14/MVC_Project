package Group05.MVC_Project.services;


import Group05.MVC_Project.models.Response;
import Group05.MVC_Project.models.User;
import Group05.MVC_Project.repositories.RoleRepository;
import Group05.MVC_Project.repositories.StatusRepository;
import Group05.MVC_Project.repositories.UserRepository;
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
    private StatusRepository statusRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ValidateToken validateToken;

    private Response response;
    private StringValidation stringValidation = new StringValidation();
    private NumberValidation numberValidation = new NumberValidation();


    @PostMapping("/create")
    public Response createUser(@RequestBody User user) {
        initializeResponse();
        if (stringValidation.validateAlphabetic(user.getName(), 40)) {
            if (stringValidation.validateAlphanumeric(user.getUserName(), 40)) {
                if (numberValidation.validatePhone(user.getPhone_number())) {
                    if (stringValidation.validateEmail(user.getEmail())) {
                        if (stringValidation.validatePassword(user.getPassword())) {
                            if (numberValidation.validateInteger(String.valueOf(user.getId_rol()))) {
                                if (numberValidation.validateInteger(String.valueOf(user.getId_status()))) {
                                    userRepository.save(user);
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

        return response;
    }

    @GetMapping("/user")
    public Response getUser(@RequestHeader(value = "Authorization") String token, @RequestParam(name = "id") Long id) {
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (userRepository.findById(id) != null) {
                response.getDataset().add(userRepository.findById(id).get());
            } else {
                response.setException("The user doesn't exists.");
            }
        }
        return response;
    }

    @GetMapping("/users")
    public Response getUsers(@RequestHeader(value = "Authorization") String token) {
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            response.getDataset().add(userRepository.findAll());
        }
        return response;
    }

    @DeleteMapping("/delete")
    public Response deleteUser(@RequestHeader(value = "Authorization") String token, @RequestParam(name = "id") Long id) {
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (userRepository.findById(id).get() != null) {
                userRepository.deleteById(id);
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
                    userDB.setId_status(user.getId_status());
                    userRepository.save(userDB);
                    response.setMessage("Status updated successfully.");
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
            if ((user.getId() != null)) {
                if (stringValidation.validateAlphabetic(user.getName(), 40)) {
                    if (stringValidation.validateAlphanumeric(user.getUserName(), 40)) {
                        if (numberValidation.validatePhone(user.getPhone_number())) {
                            if (stringValidation.validateEmail(user.getEmail())) {
                                User userDB = userRepository.findById(user.getId()).get();
                                userDB.setName(user.getName());
                                userDB.setUserName(user.getUserName());
                                userDB.setPhone_number(user.getPhone_number());
                                userDB.setEmail(user.getEmail());
                                userDB.setId_rol(user.getId_rol());
                                userRepository.save(userDB);
                                response.setStatus(true);
                                response.setMessage("Updated successfully.");
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
            } else {
                response.setException("You can't do an update with empty fields.");
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
            response.getDataset().add(statusRepository.ListStatus());
        }
        return response;

    }

    @GetMapping("/ListRol")
    public Response getListRol(@RequestHeader(value = "Authorization") String token) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            response.getDataset().add(roleRepository.ListRole());
        }
        return response;
    }

    @GetMapping("/getDevelopers")
    public Response getDevelopers(@RequestHeader(value = "Authorization") String token) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            response.getDataset().add(userRepository.developers());
        }
        return response;
    }

    @GetMapping("/getManagers")
    public Response getManagers(@RequestHeader(value = "Authorization") String token) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            response.getDataset().add(userRepository.managers());
        }
        return response;
    }

    public void initializeResponse() {
        this.response = new Response();
    }

}

