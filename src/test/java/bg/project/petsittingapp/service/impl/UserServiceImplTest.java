package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.model.dto.UserDTO;
import bg.project.petsittingapp.model.dto.UserRegisterBindingModel;
import bg.project.petsittingapp.model.entity.User;
import bg.project.petsittingapp.model.entity.UserRole;
import bg.project.petsittingapp.model.enums.RoleEnum;
import bg.project.petsittingapp.repository.UserRepository;
import bg.project.petsittingapp.repository.UserRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private UserServiceImpl userServiceToTest;
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
        userServiceToTest = new UserServiceImpl(mockUserRepository, mockUserRoleRepository, mockPasswordEncoder, mockModelMapper);
    }

    @Test
    void userRegisterReturnsFalseWhenPasswordsDontMatch() {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setUsername("test");
        userRegisterBindingModel.setPassword("1234");
        userRegisterBindingModel.setConfirmPassword("12345");
        userRegisterBindingModel.setEmail("email@email");

        Assertions.assertFalse(userServiceToTest.register(userRegisterBindingModel));
    }

    @Test
    void userRegisterReturnsFalseWhenUserAlreadyExist() {
        User user = createUser();
        mockUserRepository.save(user);
        when(mockUserRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail())).thenReturn(Optional.of(user));

        UserRegisterBindingModel userRegisterBindingModel = createUserRegisterBindingModel();


        boolean register = userServiceToTest.register(userRegisterBindingModel);

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

        boolean register = userServiceToTest.register(userRegisterBindingModel);

        Assertions.assertTrue(register);
    }

    @Test
    void findUserByUsernameTestSuccess() {
        User user = createUser();

        when(mockUserRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));


        Assertions.assertEquals(Optional.of(user), userServiceToTest.findByUsername(user.getUsername()));
    }

    @Test
    void findAllUsersTest() {
        List<User> users = new ArrayList<>();

        User user = createUser();
        users.add(user);

        when(mockUserRepository.findAll()).thenReturn(users);

        Assertions.assertEquals(users, userServiceToTest.findAllUsers());
    }

    @Test
    void findByIdTest() {
        Assertions.assertNull(userServiceToTest.findById(123L));
        Assertions.assertNull(userServiceToTest.findById(-123L));
    }

    @Test
    void deleteUserTest() {
        userServiceToTest.deleteUser(1111L);
    }

    @Test
    void saveEditedUserTest() {
        List<UserRole> roles = new ArrayList<>();

        UserRole role1 = new UserRole();
        role1.setId(2L);
        role1.setRole(RoleEnum.USER);
        roles.add(role1);

        UserDTO userDto = new UserDTO();
        userDto.setId(1L);
        userDto.setUsername("test");
        userDto.setEmail("email@email");
        userDto.setRoles(roles);

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@test");

        when(mockUserRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(mockUserRoleRepository.findByRole(RoleEnum.USER))
                .thenReturn(Optional.of(role1));

        userServiceToTest.saveEditedUser(userDto);

        Assertions.assertTrue(user.getRoles().contains(role1));
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
