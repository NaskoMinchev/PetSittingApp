package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.model.entity.User;
import bg.project.petsittingapp.model.entity.UserRole;
import bg.project.petsittingapp.model.enums.RoleEnum;
import bg.project.petsittingapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppUserDetailsServiceTest {

    private AppUserDetailsService serviceToTest;
    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        serviceToTest = new AppUserDetailsService(mockUserRepository);
    }

    @Test
    void testUsernameNotFound() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername("nasko"));
    }
    @Test
    void testUserFoundException() {
        //arrange
        User testUser = createTestUser();
        when(mockUserRepository.findByUsername(testUser.getUsername()))
                .thenReturn(Optional.of(testUser));

        //act
        UserDetails userDetails = serviceToTest.loadUserByUsername(testUser.getUsername());

        //assert
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(testUser.getUsername(), userDetails.getUsername(), "Username is not populated properly!");
        Assertions.assertEquals(testUser.getPassword(), userDetails.getPassword(), "Password is not populated properly!");
        Assertions.assertEquals(2, userDetails.getAuthorities().size(), "UserRoles are not populated properly!");
        Assertions.assertTrue(containsAuthority(userDetails, "ROLE_" + RoleEnum.ADMIN), "The user is not admin!");
        Assertions.assertTrue(containsAuthority(userDetails, "ROLE_" + RoleEnum.USER), "The user is not user!");
    }

    private boolean containsAuthority(UserDetails userDetails, String expectedAuthority) {
        return userDetails
                .getAuthorities()
                .stream()
                .anyMatch(a -> expectedAuthority.equals(a.getAuthority()));
    }
    private static User createTestUser() {
        User user = new User();
        UserRole adminRole = new UserRole();
        adminRole.setRole(RoleEnum.ADMIN);

        UserRole userRole = new UserRole();
        userRole.setRole(RoleEnum.USER);

        user.setUsername("username");
        user.setEmail("username@mail.bg");
        user.setPassword("password");
        user.setRoles(List.of(adminRole, userRole));

        return user;
    }
}
