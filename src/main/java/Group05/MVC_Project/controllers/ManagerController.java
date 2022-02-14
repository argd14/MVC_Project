package Group05.MVC_Project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Group05.MVC_Project.models.User;
import Group05.MVC_Project.utils.ValidateToken;

import java.io.Console;

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

    @GetMapping("/sprints")
    public String sprintManager(@RequestParam(name = "token", required = false) String token, Model model) {
        if (validateToken.validateToken(token)) {
            User userDB = validateToken.userDB();
            if (userDB.getId_rol() == 1) {
                model.addAttribute("title", "Sprints | Manager");
                return "manager/sprints";
            } else if (userDB.getId_rol() == 3) {
                return "redirect:/developer/dashboard?token="+token;
            }
            model.addAttribute("title", "Dashboard | Manager");
            return "manager/dashboard?token="+token;
        } else {
            return "redirect:/";
        }
    }


    @GetMapping("/projects")
    public String projectsManager(@RequestParam(name = "token", required = false) String token, Model model) {
        if (validateToken.validateToken(token)) {
            User userDB = validateToken.userDB();
            if (userDB.getId_rol() == 1) {
                model.addAttribute("title", "Projects | Manager");
                return "manager/projects";
            } else if (userDB.getId_rol() == 3) {
                return "redirect:/developer/dashboard?token="+token;
            }
            model.addAttribute("title", "Dashboard | Manager");
            return "manager/dashboard?token="+token;
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/settings")
    public String settingsManager(@RequestParam(name = "token", required = false) String token, Model model) {
        if (validateToken.validateToken(token)) {
            User userDB = validateToken.userDB();
            if (userDB.getId_rol() == 1) {
                model.addAttribute("title", "Settings | Manager");
                return "manager/settings";
            } else if (userDB.getId_rol() == 3) {
                return "redirect:/developer/dashboard?token="+token;
            }
            model.addAttribute("title", "Dashboard | Manager");
            return "manager/dashboard?token="+token;
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/issues")
    public String issuesManager(@RequestParam(name = "token", required = false) String token, Model model) {
        if (validateToken.validateToken(token)) {
            User userDB = validateToken.userDB();
            if (userDB.getId_rol() == 1) {
                model.addAttribute("title", "Issues | Manager");
                return "manager/issues";
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

