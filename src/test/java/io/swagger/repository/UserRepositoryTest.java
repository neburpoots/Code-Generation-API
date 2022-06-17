package io.swagger.repository;

import io.swagger.controller.UserController;
import io.swagger.model.entity.User;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.UUID;
@AutoConfigureMockMvc
@ContextConfiguration(classes = {UserService.class, UserController.class})
@WebMvcTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

//    @Test
//    void createNewUser() {
//        userRepository.save(createMockUser());
//    }

    private User createMockUser() {
        User user = new User();
        user.setUser_id(UUID.fromString("f963d334-2a06-4e75-96d9-16cbb8b0c2b3"));
        user.setEmail("ruben@student.inholland.nl");
        user.setLastname("Stoop");
        user.setFirstname("Ruben");
        user.setPassword("Secret123!");
        user.setDailyLimit(new BigDecimal(50000));
        user.setTransactionLimit(new BigDecimal(4000));

        return user;
    }

}