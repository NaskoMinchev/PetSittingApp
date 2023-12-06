package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.model.dto.UserRegisterBindingModel;
import bg.project.petsittingapp.model.entity.User;
import bg.project.petsittingapp.model.entity.UserRole;
import bg.project.petsittingapp.model.enums.RoleEnum;
import bg.project.petsittingapp.repository.UserRepository;
import bg.project.petsittingapp.repository.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private UserServiceImpl serviceToTest;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private UserRoleRepository mockUserRoleRepository;
    @Mock
    private ModelMapper mockModelMapper;
    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @BeforeEach
    void setUp() {
        serviceToTest = new UserServiceImpl(mockUserRepository, mockUserRoleRepository, mockPasswordEncoder, mockModelMapper);
    }

    @AfterEach
    void tearDown() {
        mockUserRepository.deleteAll();
    }

    @Test
    void userRegisterReturnsFalseWhenPasswordsDontMatch() {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setUsername("test");
        userRegisterBindingModel.setPassword("1234");
        userRegisterBindingModel.setConfirmPassword("12345");
        userRegisterBindingModel.setEmail("email@email");

        Assertions.assertFalse(serviceToTest.register(userRegisterBindingModel));
    }

    @Test
    void userRegisterReturnsFalseWhenUserAlreadyExist() {
        User user = createUser();
        mockUserRepository.save(user);
        when(mockUserRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail())).thenReturn(Optional.of(user));

        UserRegisterBindingModel userRegisterBindingModel = createUserRegisterBindingModel();


        boolean register = serviceToTest.register(userRegisterBindingModel);

        Assertions.assertFalse(register);
    }

    @Test
    void userRegistrationSuccess() {
        UserRegisterBindingModel userRegisterBindingModel = createUserRegisterBindingModel();

        when(mockUserRepository.findByUsernameOrEmail(userRegisterBindingModel.getUsername(), userRegisterBindingModel.getEmail()))
                .thenReturn(Optional.empty());

        when(mockUserRepository.count())
                .thenReturn(0L);

        UserRole userRole = new UserRole();
        userRole.setRole(RoleEnum.USER);

        UserRole adminRole = new UserRole();
        userRole.setRole(RoleEnum.ADMIN);

        when(mockUserRoleRepository.findByRole(RoleEnum.USER)).thenReturn(Optional.of(userRole));
        when(mockUserRoleRepository.findByRole(RoleEnum.ADMIN)).thenReturn(Optional.of(adminRole));

        boolean register = serviceToTest.register(userRegisterBindingModel);

        Assertions.assertTrue(register);
    }

    private static UserRegisterBindingModel createUserRegisterBindingModel() {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setUsername("test");
        userRegisterBindingModel.setPassword("1234");
        userRegisterBindingModel.setConfirmPassword("1234");
        userRegisterBindingModel.setEmail("email@email");

        return userRegisterBindingModel;
    }

    private static User createUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("1234");
        user.setPassword("1234");
        user.setEmail("email@email");

        return user;
    }

}
