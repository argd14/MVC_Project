package Group05.MVC_Project.controllers;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Data
@RequestMapping("/")
public class PageController {

    @GetMapping("home")
    public String index() {
        return "index";
    }

    
}