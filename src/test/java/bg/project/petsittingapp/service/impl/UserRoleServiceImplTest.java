package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.model.entity.UserRole;
import bg.project.petsittingapp.model.enums.RoleEnum;
import bg.project.petsittingapp.repository.UserRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceImplTest {

    private UserRoleServiceImpl userRoleServiceToTest;
    @Mock
    private UserRoleRepository mockUserRoleRepository;

    @BeforeEach
    void setUp() {
        userRoleServiceToTest = new UserRoleServiceImpl(mockUserRoleRepository);
    }

    @Test
    void testFindAll() {
        List<UserRole> roles = new ArrayList<>();

        UserRole role1 = new UserRole();
        role1.setId(1L);
        role1.setRole(RoleEnum.ADMIN);

        UserRole role2 = new UserRole();
        role2.setId(2L);
        role2.setRole(RoleEnum.USER);

        roles.add(role1);
        roles.add(role2);

        when(mockUserRoleRepository.findAll())
                .thenReturn(roles);

        List<UserRole> testRoles = userRoleServiceToTest.findAll();

        Assertions.assertEquals(testRoles, roles);
    }
}
