package Group05.MVC_Project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String dashboardManager(@RequestParam(name="token", required = false) String token){
        if(validateToken(token)){
            return "manager/dashboard";
        } else {
            return "redirect:/";
        }
        
    }
}
