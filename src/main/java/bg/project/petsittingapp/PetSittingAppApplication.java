package bg.project.petsittingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PetSittingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetSittingAppApplication.class, args);
    }

}
