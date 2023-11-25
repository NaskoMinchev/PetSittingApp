package bg.project.petsittingapp.service;

import bg.project.petsittingapp.model.dto.UserDTO;
import bg.project.petsittingapp.model.dto.UserRegisterBindingModel;
import bg.project.petsittingapp.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    boolean register(UserRegisterBindingModel userRegisterBindingModel);

    Optional<User> findByUsername(String username);

    UserDTO findById(Long id);

    List<User> findAllUsers();

    void saveEditedUser(UserDTO userDTO);

    void deleteUser(Long id);
}
