package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.model.entity.UserRole;
import bg.project.petsittingapp.repository.UserRoleRepository;
import bg.project.petsittingapp.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public List<UserRole> findAll() {
        return userRoleRepository.findAll();
    }
}
