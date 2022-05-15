package io.swagger.service;

import io.swagger.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepo;

    public RoleService(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }
}
