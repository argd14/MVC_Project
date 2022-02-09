package Group05.MVC_Project.services;

import Group05.MVC_Project.models.Response;
import Group05.MVC_Project.models.User;
import Group05.MVC_Project.repositories.UserRepository;
import Group05.MVC_Project.utils.JWTUtil;
import Group05.MVC_Project.utils.NumberValidation;
import Group05.MVC_Project.utils.SQLException;
import Group05.MVC_Project.utils.StringValidation;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtil jwtUtil;


    private Response response;
    private StringValidation stringValidation = new StringValidation();
    private NumberValidation numberValidation = new NumberValidation();


    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public Response login(@RequestBody User user) {
        initializeResponse();
        if (user.getEmail() != "" || user.getPassword() != "") {
            if (stringValidation.validateEmail(user.getEmail())) {
                if (stringValidation.validatePassword(user.getPassword())) {
                    User userDB = userRepository.getCredentials(user.getEmail());
                    if (userDB.getId_status() != 2) {
                        String passHash = userDB.getPassword();
                        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
                        if (argon2.verify(passHash, user.getPassword())) {
                            String tokenJwt = jwtUtil.create((userDB.getId()), userDB.getEmail());
                            User returnUser = new User();
                            returnUser.setId_rol(userDB.getId_rol());
                            returnUser.setUserName(userDB.getUserName());
                            response.setStatus(true);
                            response.setToken(tokenJwt);
                            response.getDataset().add(returnUser);
                            response.setMessage("Session created successfully!");
                        } else {
                            response.setException("Incorrect password.");
                        }
                    } else {
                        response.setException("Your user is inactive.");
                    }
                } else {
                    response.setException("Sorry! Looks like your password is invalid.");
                }
            } else {
                response.setException("Sorry! Looks like your email is invalid.");
            }
        } else {
            response.setException("Please fill all the fields.");
        }
        return response;
    }

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
                                response.setStatus(true);
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

    public void initializeResponse() {
        this.response = new Response();
    }
}



