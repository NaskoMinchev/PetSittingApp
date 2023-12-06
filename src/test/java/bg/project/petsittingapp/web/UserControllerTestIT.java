package bg.project.petsittingapp.web;

import bg.project.petsittingapp.model.entity.User;
import bg.project.petsittingapp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testOpenRegistrationPage() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register")
                );
    }

    @Test
    void testRegistration() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .param("username", "test")
                        .param("email", "email@email.bg")
                        .param("password", "1234")
                        .param("confirmPassword", "1234")
                        .with(csrf())
        ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));


        Assertions.assertEquals(1, userRepository.count());

        Optional<User> testUser = userRepository.findByUsername("test");

        Assertions.assertTrue(testUser.isPresent());

        User newUser = testUser.get();
        Assertions.assertEquals("test", newUser.getUsername());
        Assertions.assertEquals("email@email.bg", newUser.getEmail());
    }

    @Test
    void testRegistrationFail() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/register")
                                .param("username", "t")
                                .param("email", "email@email.bg")
                                .param("password", "1234")
                                .param("confirmPassword", "1234")
                                .with(csrf())
                ).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"));

        Assertions.assertEquals(0, userRepository.count());
    }

    @Test
    void testRegistrationPasswordsNotMatch() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/register")
                                .param("username", "test")
                                .param("email", "email@email.bg")
                                .param("password", "1234")
                                .param("confirmPassword", "12345")
                                .with(csrf())
                ).andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"));

        Assertions.assertEquals(0, userRepository.count());
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    void testNotAdminViewAllUsers() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/user/all"))
                        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN", "USER"})
    void testAdminViewAllUsers() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("users-view"));
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN", "USER"})
    void testAdminViewSelectedUser() throws Exception {

        User user = new User();
        user.setUsername("username");
        user.setEmail("username@mail.bg");
        user.setPassword("password");
        userRepository.saveAndFlush(user);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/user/edit/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("user-edit"));
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    void testNotAdminViewSelectedUser() throws Exception {

        User user = new User();
        user.setUsername("username");
        user.setEmail("username@mail.bg");
        user.setPassword("password");
        userRepository.saveAndFlush(user);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/user/edit/{id}", user.getId()))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "test", roles = {"ADMIN", "USER"})
    void testAdminDeleteUser() throws Exception {
        User userToDelete = new User();
        userToDelete.setUsername("username");
        userToDelete.setEmail("username@mail.bg");
        userToDelete.setPassword("password");
        userRepository.saveAndFlush(userToDelete);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/user/delete/{id}", userToDelete.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/all"));
    }
}
