package Group05.MVC_Project.services;

import Group05.MVC_Project.repositories.RoleRepository;
import Group05.MVC_Project.repositories.StatusRepository;
import Group05.MVC_Project.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createUser() {
    }

    @Test
    void getUser() {
    }

    @Test
    void getUsers() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void changeStatus() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void getListStatus() {
    }

    @Test
    void getListRol() {
    }

    @Test
    void getDevelopers() {
    }

    @Test
    void getManagers() {
    }

    @Test
    void updatePassword() {
    }

    @AfterEach
    void tearDown() {
    }
}