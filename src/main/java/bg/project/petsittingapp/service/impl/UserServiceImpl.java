package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.model.dto.UserDTO;
import bg.project.petsittingapp.model.dto.UserRegisterBindingModel;
import bg.project.petsittingapp.model.entity.User;
import bg.project.petsittingapp.model.entity.UserRole;
import bg.project.petsittingapp.model.enums.RoleEnum;
import bg.project.petsittingapp.repository.UserRepository;
import bg.project.petsittingapp.repository.UserRoleRepository;
import bg.project.petsittingapp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean register(UserRegisterBindingModel userRegisterBindingModel) {

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            return false;
        }

        Optional<User> userExist = userRepository.findByUsernameOrEmail(userRegisterBindingModel.getUsername(), userRegisterBindingModel.getEmail());

        if (userExist.isPresent()) {
            return false;
        }

        User user = new User();
        user.setUsername(userRegisterBindingModel.getUsername());
        user.setEmail(userRegisterBindingModel.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterBindingModel.getPassword()));
        user.addRole(userRoleRepository.findByRole(RoleEnum.USER).get());

        if (userRepository.count() == 0) {
            user.addRole(userRoleRepository.findByRole(RoleEnum.ADMIN).get());
        }

        userRepository.save(user);

        return true;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDTO findById(Long id) {
        return userRepository.findById(id).map(user -> modelMapper.map(user, UserDTO.class)).orElse(null);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveEditedUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElse(null);

        if (user != null) {

            if (!userDTO.getRoles().contains(userRoleRepository.findByRole(RoleEnum.USER).get())) {
                userDTO.getRoles().add(userRoleRepository.findByRole(RoleEnum.USER).get());
            }
            user.setRoles(userDTO.getRoles());

            userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
