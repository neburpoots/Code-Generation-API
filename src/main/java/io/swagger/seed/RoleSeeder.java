package io.swagger.seed;

import io.swagger.model.entity.Role;
import io.swagger.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleSeeder {

    private final RoleRepository roleRepo;

    @Autowired
    public RoleSeeder(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    public List<Role> seed() {
        Role customer = new Role(1, "ROLE_CUSTOMER");
        Role employee = new Role(2, "ROLE_EMPLOYEE");

        return (List<Role>)roleRepo.saveAll(
                List.of(customer, employee)
        );
    }
}
