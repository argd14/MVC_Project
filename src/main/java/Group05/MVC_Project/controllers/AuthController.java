package Group05.MVC_Project.controllers;

import Group05.MVC_Project.models.Response;
import Group05.MVC_Project.models.User;
import Group05.MVC_Project.repositories.UserRepository;
import Group05.MVC_Project.utils.JWTUtil;
import Group05.MVC_Project.utils.StringValidation;
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
    private Response response;
    private StringValidation stringValidation = new StringValidation();


    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public Response login(@RequestBody User user) {
        initializeResponse();
        if (user != null) {
            if (stringValidation.validateEmail(user.getEmail())) {
                if (stringValidation.validatePassword(user.getPassword())) {
                    User userDB = userRepository.getCredentials(user.getEmail());
                    String passHash = userDB.getPassword();
                    Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
                    if (argon2.verify(passHash, user.getPassword())) {
                        String tokenJwt = jwtUtil.create((userDB.getId()), userDB.getEmail());
                        response.setStatus(true);
                        response.setToken(tokenJwt);
                        response.setMessage("Loged in successfully!");

                    } else {
                        response.setException("Password invalid, no coincidences");
                    }
                } else {
                    response.setException("invalid password");
                }
            } else {
                response.setException("invalid email");
            }

        } else {
            response.setException("user no found");
        }
        return response;
    }

    public void initializeResponse() {
        this.response = new Response();
    }
}



