package Group05.MVC_Project.controllers;

import Group05.MVC_Project.models.User;
import Group05.MVC_Project.utils.ValidateToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/developer")
public class DeveloperController {
    @Autowired
    private ValidateToken validateToken;

    @GetMapping("/dashboard")
    public String dashboardDeveloper(@RequestParam(name = "token", required = false) String token, Model model) {
        if (validateToken.validateToken(token)) {
            User userDB = validateToken.userDB();
            if (userDB.getId_rol() == 3) {
                model.addAttribute("title", "Dashboard | Developer");
                return "developer/dashboard";
            } else if (userDB.getId_rol() == 1) {
                model.addAttribute("title", "Dashboard | Manager");
                return "redirect:/manager/dashboard?token="+token;
            }
            model.addAttribute("title", "Dashboard | Manager");
            return "redirect:/manager/dashboard?token="+token;
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("register")
    public String registerDeveloper() {
        return "developer/register";
    }
}
