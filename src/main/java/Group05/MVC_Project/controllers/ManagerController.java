package Group05.MVC_Project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Group05.MVC_Project.models.User;
import Group05.MVC_Project.utils.ValidateToken;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ValidateToken validateToken;
    
    @GetMapping("/dashboard")
    public String dashboardManager(@RequestParam(name = "token", required = false) String token, Model model) {
        if (validateToken.validateToken(token)) {
            User userDB = validateToken.userDB();
            if (userDB.getId_rol() == 1) {
                model.addAttribute("title", "Dashboard | Manager");
                return "manager/dashboard";
            } else if (userDB.getId_rol() == 3) {
                model.addAttribute("title", "Dashboard | Developer");
                return "redirect:/developer/dashboard?token="+token;
            }
            model.addAttribute("title", "Dashboard | Manager");
            return "redirect:/manager/dashboard?token="+token;
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/users")
    public String usersManager(@RequestParam(name = "token", required = false) String token, Model model) {
        if (validateToken.validateToken(token)) {
            User userDB = validateToken.userDB();
            if (userDB.getId_rol() == 1) {
                model.addAttribute("title", "Users | Manager");
                return "manager/users";
            } else if (userDB.getId_rol() == 3) {
                return "redirect:/developer/dashboard?token="+token;
            }
            model.addAttribute("title", "Dashboard | Manager");
            return "manager/dashboard?token="+token;
        } else {
            return "redirect:/";
        }
    }

}
