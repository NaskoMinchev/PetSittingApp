package bg.project.petsittingapp.web;

import bg.project.petsittingapp.model.entity.Article;
import bg.project.petsittingapp.model.entity.Image;
import bg.project.petsittingapp.model.entity.User;
import bg.project.petsittingapp.model.entity.UserRole;
import bg.project.petsittingapp.model.enums.RoleEnum;
import bg.project.petsittingapp.repository.ArticleRepository;
import bg.project.petsittingapp.repository.ImageRepository;
import bg.project.petsittingapp.repository.UserRepository;
import bg.project.petsittingapp.repository.UserRoleRepository;
import bg.project.petsittingapp.service.ArticleService;
import jakarta.validation.Valid;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BlogControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ImageRepository imageRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
        imageRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN", "USER"})
    void testOpenBlogPage() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/blog")
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("blog"));
    }

    @Test
    void testOpenBlogPageFail() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/blog"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN", "USER"})
    void testOpenArticleCreatePage() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/blog/create")
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("blog-create"));
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    void testOpenArticleSinglePage() throws Exception {

        Article article = createArticle();
        articleRepository.save(article);

        mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/blog/article/{id}", article.getId())
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("blog-single"));
    }

//    @Test
//    @WithMockUser(username = "test", roles = {"ADMIN", "USER"})
//    void testAdminCreateArticle() throws Exception {
//
////        MultipartFile multipartFile = mock(MultipartFile.class);
////
////        when(multipartFile.getBytes()).thenReturn(new byte[] {1, 2, 3, 4});
////        when(multipartFile.getOriginalFilename()).thenReturn("filename.jpg");
////        when(multipartFile.isEmpty()).thenReturn(false);
////        when(multipartFile.getContentType()).thenReturn("image/jpg");
////
//        MockMultipartFile file = new MockMultipartFile("anonymous-profile-pic.jpg", "anonymous-profile-pic.jpg", "image/jpg", "inputFile".getBytes());
////
////        mockMvc.perform(multipart("/blog/create").file(file))
////                .andExpect(status().isOk());
//
//        mockMvc
//                .perform(
//                        MockMvcRequestBuilders.post("/blog/create")
//                                .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/blog"));
//    }

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    void testUserCreateArticle() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/blog/create"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN", "USER"})
    void testAdminDeleteArticle() throws Exception {

        Article articleToDelete = createArticle();
        articleRepository.saveAndFlush(articleToDelete);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/blog/article/delete/{id}", articleToDelete.getId())
                                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/blog"));
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN", "USER"})
    void testAdminOpenArticleEditPage() throws Exception {

        Article article = createArticle();
        articleRepository.saveAndFlush(article);


        mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/blog/article/edit/{id}", article.getId())
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("blog-edit"));
    }

    private Article createArticle() {

        Article article = new Article();
        article.setTitle("test");
        article.setBody("test");
        article.setHeader("test");
        article.setFooter("test");
        article.setCreated(LocalDate.now());
        article.setAuthor("test");

        Image image = new Image();
        image.setImageUrl("testUrl");
        imageRepository.save(image);

        article.setImage(image);

        return article;
    }
}
