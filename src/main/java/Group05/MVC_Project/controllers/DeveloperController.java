package Group05.MVC_Project.controllers;

import Group05.MVC_Project.models.User;
import Group05.MVC_Project.repositories.ProjectRepository;
import Group05.MVC_Project.utils.ValidateToken;
import antlr.Token;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/developer")
public class DeveloperController {
    @Autowired
    private ValidateToken validateToken;

    @Autowired ProjectRepository projectRepository;

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

    @GetMapping("/register")
    public String registerDeveloper() {
        return "developer/register";
    }

    @GetMapping("/settings")
    public String settingsDeveloper(@RequestParam(name = "token", required = false) String token, Model model){
        if (validateToken.validateToken(token)) {
            User userDB = validateToken.userDB();
            if (userDB.getId_rol() == 3) {
                model.addAttribute( "title", "Settings | Developer");
                return "developer/settings";
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

    @GetMapping("/backlog")
    public String backlogDeveloper(@RequestParam(name = "token", required = false) String token, Model model, @RequestParam(name="id") Long id_project){
        if (validateToken.validateToken(token)) {
            User userDB = validateToken.userDB();
            if (userDB.getId_rol() == 3) {
                if (checkIfDeveloperHasAccess(id_project, userDB.getId())) {
                    model.addAttribute("title", "Backlog | Developer");
                    model.addAttribute("id_project",id_project);
                    return "developer/backlog";
                } else {
                    return "redirect:/developer/dashboard?token="+token;
                }
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

    public boolean checkIfDeveloperHasAccess(Long id_project, Long id_user){
        
        List<Object> response = projectRepository.checkIfDeveloperHasAccess(id_user, id_project);

        if (response.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
