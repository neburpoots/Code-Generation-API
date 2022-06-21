package io.swagger.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

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

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }


}
