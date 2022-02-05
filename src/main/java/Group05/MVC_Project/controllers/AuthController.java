package Group05.MVC_Project.controllers;

import Group05.MVC_Project.models.Response;
import Group05.MVC_Project.models.User;
import Group05.MVC_Project.repositories.UserRepository;
import Group05.MVC_Project.utils.JWTUtil;
import Group05.MVC_Project.utils.StringValidation;
import Group05.MVC_Project.utils.ValidateToken;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtil jwtUtil;

    private ValidateToken validateToken;

    private Response response;
    private StringValidation stringValidation = new StringValidation();


    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public Response login(@RequestBody User user) {
        initializeResponse();
        if (user.getEmail() != "" || user.getPassword() != "") {
            if (stringValidation.validateEmail(user.getEmail())) {
                if (stringValidation.validatePassword(user.getPassword())) {
                    User userDB = userRepository.getCredentials(user.getEmail());
                    String passHash = userDB.getPassword();
                    Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
                    if (argon2.verify(passHash, user.getPassword())) {
                        String tokenJwt = jwtUtil.create((userDB.getId()), userDB.getEmail());
                        response.setStatus(true);
                        response.setToken(tokenJwt);
                        response.getDataset().add(userDB);
                        response.setMessage("Session created successfully!");
                    } else {
                        response.setException("Incorrect password.");
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

    public void initializeResponse() {
        this.response = new Response();
    }
}



