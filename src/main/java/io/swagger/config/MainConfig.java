package io.swagger.config;

import io.swagger.seed.AccountSeeder;
import io.swagger.seed.RoleSeeder;
import io.swagger.seed.UserSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// CLASS RUNS ON STARTUP BECAUSE OF THE COMMANDLINERUNNER
//OLD SEEDER
public class MainConfig {

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
//            roleSeeder.seed();
//            userSeeder.seed();
//            accountSeeder.seed();
        };
    }


}
