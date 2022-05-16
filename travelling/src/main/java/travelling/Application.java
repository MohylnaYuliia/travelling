package travelling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import travelling.entity.UserEntity;
import travelling.repository.UserRepository;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private UserRepository userRepository;

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {
            userRepository.save(UserEntity.builder().id(1).name("John").build());
            userRepository.save(UserEntity.builder().id(2).name("Brian").build());
        };
    }
}
