package Group05.MVC_Project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import Group05.MVC_Project.utils.JWTUtil;

@Controller
public class PageController {

    @Autowired
    private JWTUtil jwtUtil;

    private boolean validateToken(String token) {
        String userId = jwtUtil.getKey(token);
        return userId != null;
    }
    
    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("developer/register")
    public String registerDeveloper(){
        return "developer/register";
    }

    @GetMapping("manager/dashboard")
    public String dashboardManager(@RequestHeader(value = "Authorization") String token){
        if(!validateToken(token)){
            return "index";
        } else {
            return "manager/dashboard";
        }
        
    }
}
