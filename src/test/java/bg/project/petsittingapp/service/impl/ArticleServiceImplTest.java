package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.model.dto.ArticleDTO;
import bg.project.petsittingapp.model.dto.CreateArticleDTO;
import bg.project.petsittingapp.model.entity.Article;
import bg.project.petsittingapp.model.entity.Image;
import bg.project.petsittingapp.repository.ArticleRepository;
import bg.project.petsittingapp.service.CommentService;
import bg.project.petsittingapp.service.ImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceImplTest {

    private ArticleServiceImpl articleServiceToTest;
    @Mock
    private ArticleRepository mockArticleRepository;
    @Mock
    private CommentService mockCommentService;
    @Mock
    private ImageService mockImageService;
    @Mock
    private ModelMapper mockModelMapper;

    @BeforeEach
    void setUp() {
        this.articleServiceToTest = new ArticleServiceImpl(mockArticleRepository, mockCommentService, mockImageService, mockModelMapper);
    }

    @Test
    void createArticleTest() {
        CreateArticleDTO createArticleDTO = new CreateArticleDTO();
        createArticleDTO.setTitle("Test");
        MultipartFile mockPicture = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", new byte[0]);
        createArticleDTO.setPicture(mockPicture);

        Article mappedArticle = new Article();
        when(mockModelMapper.map(createArticleDTO, Article.class)).thenReturn(mappedArticle);

        String mockFilePath = "mock/path/to/image.jpg";
        when(mockImageService.getFilePath(anyString(), anyString(), any(MultipartFile.class))).thenReturn(mockFilePath);

        Image mockImage = new Image();
        when(mockImageService.createImage(any(MultipartFile.class), eq(mockFilePath))).thenReturn(mockImage);

        articleServiceToTest.create(createArticleDTO);

        verify(mockModelMapper, times(1)).map(createArticleDTO, Article.class);
        verify(mockImageService, times(1)).getFilePath(anyString(), anyString(), any(MultipartFile.class));
        verify(mockImageService, times(1)).createImage(any(MultipartFile.class), eq(mockFilePath));
        verify(mockArticleRepository, times(1)).save(mappedArticle);
    }

    @Test
    void editArticleTest() {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(1L);
        articleDTO.setTitle("newTitle");
        articleDTO.setBody("body");
        articleDTO.setHeader("header");
        articleDTO.setFooter("footer");
        articleDTO.setAuthor("author");
        articleDTO.setAdditionalInfo("a");

        Article article = new Article();
        article.setId(1L);
        article.setTitle("title");

        MultipartFile mockPicture = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", new byte[0]);
        articleDTO.setPicture(mockPicture);

        when(mockArticleRepository.findById(1L)).thenReturn(Optional.of(article));

        articleServiceToTest.editArticle(articleDTO);

        Assertions.assertEquals("newTitle", article.getTitle());
    }
}
