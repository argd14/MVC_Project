package Group05.MVC_Project.services;


import Group05.MVC_Project.models.Response;
import Group05.MVC_Project.models.User;
import Group05.MVC_Project.repositories.UserRepository;
import Group05.MVC_Project.utils.*;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidateToken validateToken;

    private Response response;
    private StringValidation stringValidation = new StringValidation();
    private NumberValidation numberValidation = new NumberValidation();

    // api for register
    @RequestMapping(value = "api/register", method = RequestMethod.POST)
    public Response registerUser(@RequestBody User user) {
        initializeResponse();
        if (stringValidation.validateAlphabetic(user.getName(), 40)) {
            if (stringValidation.validateAlphanumeric(user.getUserName(), 40)) {
                if (numberValidation.validatePhone(user.getPhone_number())) {
                    if (stringValidation.validateEmail(user.getEmail())) {
                        if (stringValidation.validatePassword(user.getPassword())) {
                            user.setId_status(1);
                            user.setId_rol(3);
                            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
                            String hash = argon2.hash(1, 1024, 1, user.getPassword());
                            user.setPassword(hash);
                            try {
                                userRepository.save(user);
                                response.setStatus(true);
                                response.setMessage("Registered successfully");
                            } catch (DataAccessException ex) {
                                response.setException(SQLException.getException(String.valueOf(ex.getCause())));
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

    // api to get user data by his id
    @RequestMapping(value = "api/user/{id}", method = RequestMethod.GET)
    public Response getUser(@RequestHeader(value = "Authorization") String token, @PathVariable Long id) {
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (userRepository.getById(id) != null) {
                response.getDataset().add(userRepository.findById(id).get());
            } else {
                response.setException("The user doesn't exists.");
            }
        }
        return response;
    }

    // api to get all the users
    @RequestMapping(value = "api/users", method = RequestMethod.GET)
    public Response getUsers(@RequestHeader(value = "Authorization") String token) {
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            response.getDataset().add(userRepository.findAll());
        }
        return response;
    }

    //api for deleting a user by his id
    @RequestMapping(value = "api/delete/{id}", method = RequestMethod.DELETE)
    public Response deleteUser(@RequestHeader(value = "Authorization") String token, @PathVariable Long id) {
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

    @RequestMapping(value = "api/update", method = RequestMethod.POST)
    public Response updateUser(@RequestHeader(value = "Authorization") String token, @RequestBody User user) {
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if ((user.getId()!=null)) {
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



    public void initializeResponse() {
        this.response = new Response();
    }

}

