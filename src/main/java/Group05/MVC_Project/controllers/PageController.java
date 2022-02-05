package Group05.MVC_Project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import Group05.MVC_Project.utils.JWTUtil;

@Controller
public class PageController {

    @Autowired
    private JWTUtil jwtUtil;

    private boolean validateToken(String token) {
       try{
           String userId = jwtUtil.getKey(token);
           return userId != null ;
       }catch(Exception e){
           return false;
       }


       // return userId != null;
    }
    
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    @RequestMapping(value="developer/register", method = RequestMethod.GET)
    public String registerDeveloper(){
        return "developer/register";
    }

    @RequestMapping(value="manager/dashboard", method = RequestMethod.GET)
    public String dashboardManager(@RequestHeader(value = "Authorization") String token){
        if(validateToken(token)){
            return "manager/dashboard";
        } else {
            return "index";
        }
        
    }
}
