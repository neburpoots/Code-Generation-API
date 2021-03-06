package io.swagger.seed;

import io.swagger.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MainSeeder {
    private final RoleSeeder roleSeeder;
    private final UserSeeder userSeeder;
    private final AccountSeeder accountSeeder;
    private final TransactionSeeder transactionSeeder;


    @Autowired
    public MainSeeder(RoleSeeder roleSeeder, UserSeeder userSeeder, AccountSeeder accountSeeder, TransactionSeeder transactionSeeder) {
        this.roleSeeder = roleSeeder;
        this.userSeeder = userSeeder;
        this.accountSeeder = accountSeeder;
        this.transactionSeeder = transactionSeeder;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        roleSeeder.seed();
        List<User> users = userSeeder.seed();
        accountSeeder.seed(users);
        transactionSeeder.seed();
    }
}
