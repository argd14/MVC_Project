package Group05.MVC_Project.utils;

import Group05.MVC_Project.controllers.PageController;
import Group05.MVC_Project.models.User;
import Group05.MVC_Project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateToken {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    private Long userId;

    public boolean validateToken(String token) {
        try {
            userId = Long.valueOf(jwtUtil.getKey(token));
            return userId != null;
        } catch (Exception e) {
            return false;
        }
    }

    public User userDB() {
        return userRepository.getById(userId);
    }
}
