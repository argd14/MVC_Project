package Group05.MVC_Project.controllers;

import Group05.MVC_Project.models.Response;
import Group05.MVC_Project.models.User;
import Group05.MVC_Project.repositories.UserRepository;
import Group05.MVC_Project.utils.JWTUtil;
import Group05.MVC_Project.utils.StringValidation;
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
    private JWTUtil jwtUtil ;
    private Response response;
    private StringValidation stringValidation = new StringValidation();


    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public Response login(@RequestBody User user) {
        initializeResponse();
        if (user != null) {
            User userDB = userRepository.getCredentials(user.getEmail());
            String tokenJwt = jwtUtil.create((userDB.getId()), userDB.getEmail());
            response.setMessage(tokenJwt);
            response.setException("loggin successfully");

        } else {
            response.setException("not found user");
        }

        return response;
    }

    public void initializeResponse() {
        this.response = new Response();
    }
}



