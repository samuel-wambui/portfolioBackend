package com.ngarisamuel.portfolio;

import com.ngarisamuel.portfolio.auth.AdminUser;
import com.ngarisamuel.portfolio.auth.AdminUserRepository;
import java.util.Optional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PortfolioApplication {

    public static void main(String[] args) {
        System.setProperty("debug", System.getProperty("debug", "false"));
        SpringApplication.run(PortfolioApplication.class, args);
    }

    @Bean
    CommandLineRunner initUsers(
            AdminUserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Optional<AdminUser> existingUser = userRepository.findByEmail("samuelngari13@gmail.com");

            if (existingUser.isEmpty()) {
                AdminUser superUser = new AdminUser();
                superUser.setUsername("sw54903");
                superUser.setFirstName("Samuel");
                superUser.setLastName("Ngari");
                superUser.setEmail("samuelngari13@gmail.com");
                superUser.setPassword(passwordEncoder.encode("Bobo@092025@!"));
                superUser.setRole("SUPERUSER");
                superUser.setEnabled(true);
                superUser.setLocked(false);

                userRepository.save(superUser);

                System.out.println("Superuser created successfully.");
            } else {
                System.out.println("Superuser already exists.");
            }
        };
    }
}
