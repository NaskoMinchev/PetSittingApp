package bg.project.petsittingapp.web;

import bg.project.petsittingapp.model.dto.CreateCommentBindingModel;
import bg.project.petsittingapp.model.entity.Article;
import bg.project.petsittingapp.model.entity.Comment;
import bg.project.petsittingapp.model.entity.Image;
import bg.project.petsittingapp.model.entity.User;
import bg.project.petsittingapp.model.enums.RoleEnum;
import bg.project.petsittingapp.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.text.MatchesPattern;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentRestControllerTest {

    private static final String COMMENT_1 = "test 1!";
    private static final String COMMENT_2 = "test 2!";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("email@email.bg");
        testUser.setPassword("password");
        testUser.setRoles(List.of(userRoleRepository.findByRole(RoleEnum.USER).get()));

        testUser = userRepository.save(testUser);
    }
    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    void testGetComments() throws Exception {
        Article article = initComments(createArticle());

        mockMvc.perform(MockMvcRequestBuilders.get("/blog/article/" + article.getId() + "/comments")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$", hasSize(2))).
                andExpect(jsonPath("$.[0].text", is(COMMENT_2))).
                andExpect(jsonPath("$.[1].text", is(COMMENT_1)));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testCreateComments() throws Exception {
        CreateCommentBindingModel testComment = new CreateCommentBindingModel();
                testComment.setText(COMMENT_1);
        Article emptyArticle = createArticle();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/blog/article/" + emptyArticle.getId() + "/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testComment))
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", MatchesPattern.matchesPattern("/blog/article/" + emptyArticle.getId() + "/comments/\\d")))
                .andExpect(jsonPath("$.text").value(is(COMMENT_1)));

    }

    private Article createArticle() {

        Article article = new Article();
        article.setTitle("test");
        article.setBody("test");
        article.setHeader("test");
        article.setFooter("test");
        article.setCreated(LocalDate.now());

        Image image = new Image();
        image.setImageUrl("testUrl");
        imageRepository.save(image);

        article.setImage(image);

        article.setAuthor(testUser.getUsername());

        articleRepository.save(article);
        return article;
    }

    private Article initComments(Article article) {

        Comment comment1 = new Comment();
        comment1.setPublished(LocalDateTime.now());
        comment1.setAuthor(testUser.getUsername());
        comment1.setText(COMMENT_1);
        comment1.setArticle(article);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setPublished(LocalDateTime.now());
        comment2.setAuthor(testUser.getUsername());
        comment2.setText(COMMENT_2);
        comment2.setArticle(article);
        commentRepository.save(comment2);

        article.setComments(List.of(comment1, comment2));

        articleRepository.save(article);
        return article;
    }
}
