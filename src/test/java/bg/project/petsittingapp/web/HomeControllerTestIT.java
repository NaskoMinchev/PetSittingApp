package bg.project.petsittingapp.web;

import bg.project.petsittingapp.model.dto.BlogDTO;
import bg.project.petsittingapp.repository.ArticleRepository;
import bg.project.petsittingapp.repository.PetRepository;
import bg.project.petsittingapp.service.ArticleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testOpenAboutPage() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"));
    }

    @Test
    void testOpenServicesPage() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/services"))
                .andExpect(status().isOk())
                .andExpect(view().name("services")
                );
    }

    @Test
    void testOpenIndexPage() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index")
                );
    }
}
