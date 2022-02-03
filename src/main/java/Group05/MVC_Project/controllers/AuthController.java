package Group05.MVC_Project.controllers;

import Group05.MVC_Project.models.Response;
import Group05.MVC_Project.models.User;
import Group05.MVC_Project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AuthController {

    private Response response = new Response();
    private UserRepository userRepository;
    User user = new User(1L, "juan", "juan503", "2323423", "sdsdfs", "2312", 2);
    User user2 = new User(1L, "juan", "juan503", "2323423", "sdsdfs", "2312", 2);


    @GetMapping(value = "api/prueba")
    public @ResponseBody
    Response prueba() {
        response.getDataset().add(user);
        response.getDataset().add(user2);
        return response;
    }


}
