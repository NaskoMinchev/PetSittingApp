package bg.project.petsittingapp.init;

import bg.project.petsittingapp.model.entity.UserRole;
import bg.project.petsittingapp.model.enums.RoleEnum;
import bg.project.petsittingapp.repository.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UserRoleInit implements CommandLineRunner {

    private final UserRoleRepository userRoleRepository;

    public UserRoleInit(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (userRoleRepository.count() == 0) {
            List<UserRole> roles = new ArrayList<>();

            Arrays.stream(RoleEnum.values()).forEach(value -> {
                UserRole role = new UserRole();
                role.setRole(value);
                roles.add(role);
            });

            userRoleRepository.saveAll(roles);
        }
    }
}
