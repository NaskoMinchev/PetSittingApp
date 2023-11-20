package bg.project.petsittingapp.service;

import bg.project.petsittingapp.model.dto.UserRegisterBindingModel;
import bg.project.petsittingapp.model.entity.User;

import java.util.Optional;

public interface UserService {

    boolean register(UserRegisterBindingModel userRegisterBindingModel);

    Optional<User> findByUsername(String username);

}
