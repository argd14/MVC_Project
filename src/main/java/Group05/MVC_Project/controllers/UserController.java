package Group05.MVC_Project.controllers;


import Group05.MVC_Project.models.Response;
import Group05.MVC_Project.models.User;
import Group05.MVC_Project.repositories.UserRepository;
import Group05.MVC_Project.utils.JWTUtil;
import Group05.MVC_Project.utils.NumberValidation;
import Group05.MVC_Project.utils.StringValidation;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private Response response;
    private StringValidation stringValidation = new StringValidation();
    private NumberValidation numberValidation = new NumberValidation();


    private JWTUtil jwtUtil = new JWTUtil();

    //api para registrar
    @RequestMapping(value = "api/register", method = RequestMethod.POST)
    public Response registerUser(@RequestBody User user) {
        initializeResponse();
        if (stringValidation.validateAlphabetic(user.getName(), 40)) {
            if (stringValidation.validateAlphanumeric(user.getUserName(), 40)) {
                if (numberValidation.validatePhone(user.getPhoneNumber())) {
                    if (stringValidation.validateEmail(user.getEmail())) {
                        if (stringValidation.validatePassword(user.getPassword())) {
                            userRepository.save(user);
                            response.setStatus(true);
                            response.setMessage("Registered successfully");
                        } else {
                            response.setException("invalid password");
                        }
                    } else {
                        response.setException("invalid email");
                    }
                } else {
                    response.setException("invalid phone number");
                }
            } else {
                response.setException("invalid user name");
            }
        } else {
            response.setException("invalid name");
        }
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(1, 1024, 1, user.getPassword());
        user.setPassword(hash);
        return response;
    }

    //api para obtener un usuario por id
    @RequestMapping(value = "api/user/{id}", method = RequestMethod.GET)
    public Response getUser(@RequestHeader(value = "Authorization") String token, @PathVariable Long id) {
        if (!validedToken(token)) {
            response.setException("don't logged in");
        } else {
            if (userRepository.getById(id) != null) {
                response.getDataset().add(userRepository.getById(id));
            } else {
                response.setException("User does not exist");
            }
        }
        return response;
    }

    //api para obtener todos los usuarios
    @RequestMapping(value = "api/users", method = RequestMethod.GET)
    public Response getUsers(@RequestHeader(value = "Authorization") String token) {
        if (!validedToken(token)) {
            response.setException("don't logged in");
        } else {
            response.getDataset().add(userRepository.findAll());
        }
        return response;
    }

    //api para borrar un usuario por id
    @RequestMapping(value = "api/delete/{id}", method = RequestMethod.DELETE)
    public Response deleteUser(@RequestHeader(value = "Authorization") String token, @PathVariable Long id) {
        if (!validedToken(token)) {
            response.setException("don't logged in");
        } else {
            if (userRepository.getById(id) != null) {
                userRepository.deleteById(id);
            } else {
                response.setException("User does not exist");
            }
        }
        return response;
    }


    private boolean validedToken(String token) {
        String userId = jwtUtil.getKey(token);
        return userId != null;
    }

    public void initializeResponse() {
        this.response = new Response();
    }

}

