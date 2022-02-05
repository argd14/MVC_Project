package Group05.MVC_Project.controllers;

import Group05.MVC_Project.models.User;
import Group05.MVC_Project.repositories.UserRepository;
import Group05.MVC_Project.utils.ValidateToken;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Data
public class PageController {

    @Autowired
    private ValidateToken validateToken;
    @Autowired
    private UserRepository userRepository;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "developer/register", method = RequestMethod.GET)
    public String registerDeveloper() {
        return "developer/register";
    }


    @RequestMapping(value = "manager/dashboard", method = RequestMethod.GET)
    public String dashboardManager(@RequestHeader(value = "Authorization") String token) {
        if (validateToken.validateToken(token)) {
            User userDB = validateToken.userDB();
            if (userDB.getId_rol() == 2) {
                return "manager/dashboard";
            } else if (userDB.getId_rol() == 3) {
                return "redirect:/developer/dashboard";
            }
            return "manager/dashboard";
        } else {
            return "redirect:/";
        }
        // return "";
    }
}