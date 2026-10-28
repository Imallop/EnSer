package gal.usc.etse.es.motorhome.configuration;

import gal.usc.etse.es.motorhome.model.entity.User;
import gal.usc.etse.es.motorhome.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class DevelopmentDataConfiguration {
    @Bean
    CommandLineRunner developmentUser(UserRepository users, PasswordEncoder encoder) {
        return args -> {
            if (!users.existsById("dev")) {
                users.save(new User("dev", encoder.encode("motorhome-dev"),
                        "Usuario de desarrollo", "dev@example.test"));
            }
        };
    }
}
