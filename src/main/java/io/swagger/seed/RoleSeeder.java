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
        Role pending = new Role("ROLE_PENDING");
        Role customer = new Role("ROLE_CUSTOMER");
        Role employee = new Role("ROLE_EMPLOYEE");

        roleRepo.save(pending);
        return (List<Role>)roleRepo.saveAll(
                List.of(customer, employee)
        );
    }
}
