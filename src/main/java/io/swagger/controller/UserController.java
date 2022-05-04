package io.swagger.controller;

import io.swagger.model.UsersDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @GetMapping
    public String getAllUsers() {
        return "Tim is stom";
    }
}
