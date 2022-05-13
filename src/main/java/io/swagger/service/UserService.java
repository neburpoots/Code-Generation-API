package io.swagger.service;

import io.swagger.model.entity.User;
import io.swagger.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getUsers() {
        return this.userRepo.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return this.userRepo.findById(id);
    }

}
