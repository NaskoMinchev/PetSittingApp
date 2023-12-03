package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.model.dto.ArticleDTO;
import bg.project.petsittingapp.model.dto.BlogDTO;
import bg.project.petsittingapp.model.dto.CreateArticleDTO;
import bg.project.petsittingapp.model.entity.Article;
import bg.project.petsittingapp.model.entity.Image;
import bg.project.petsittingapp.repository.ArticleRepository;
import bg.project.petsittingapp.service.ArticleService;
import bg.project.petsittingapp.service.CommentService;
import bg.project.petsittingapp.service.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {
    private static final String FOLDER_PATH = "articlePictures/";
    private final ArticleRepository articleRepository;
    private final CommentService commentService;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    public ArticleServiceImpl(ArticleRepository articleRepository, CommentService commentService, ImageService imageService, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.commentService = commentService;
        this.imageService = imageService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(CreateArticleDTO createArticleDTO) {

        Article article = modelMapper.map(createArticleDTO, Article.class);

        MultipartFile pictureFile = createArticleDTO.getPicture();

        String filePath = imageService.getFilePath(FOLDER_PATH, createArticleDTO.getTitle(), pictureFile);

        Image image = imageService.createImage(pictureFile, filePath);

        article.setImage(image);

        articleRepository.save(article);
    }

    @Override
    public BlogDTO getAllArticles() {

        List<Article> articles = articleRepository.findAll();

        BlogDTO blogDTO = new BlogDTO();

        blogDTO.setArticles(articles.stream().map(this::articleMap).collect(Collectors.toList()));

        Collections.reverse(blogDTO.getArticles());

        return blogDTO;
    }

    @Override
    public ArticleDTO getSingleArticle(Long id) {
        Optional<Article> article = articleRepository.findById(id);

        if (article.isEmpty()) {
            throw new NoSuchElementException("No such article");
        }

        return articleMap(article.get());
    }

    @Override
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    public void editArticle(ArticleDTO articleDTO) {
        Optional<Article> article = articleRepository.findById(articleDTO.getId());

        if (article.isEmpty()) {
            throw new NoSuchElementException("No such article!");
        }

        MultipartFile pictureFile = articleDTO.getPicture();

        if (!pictureFile.isEmpty()) {
            String filePath = imageService.getFilePath(FOLDER_PATH, articleDTO.getTitle(), pictureFile);
            Image image = imageService.createImage(pictureFile, filePath);

            article.get().setImage(image);
        }

        if (!articleDTO.getTitle().isEmpty()) {
            article.get().setTitle(articleDTO.getTitle());
        }

        if (!articleDTO.getBody().isEmpty()) {
            article.get().setBody(articleDTO.getBody());
        }

        if (!articleDTO.getAdditionalInfo().isEmpty()) {
            article.get().setAdditionalInfo(articleDTO.getAdditionalInfo());
        }

        if (!articleDTO.getHeader().isEmpty()) {
            article.get().setHeader(articleDTO.getHeader());
        }

        if (!articleDTO.getFooter().isEmpty()) {
            article.get().setFooter(articleDTO.getFooter());
        }
        article.get().setCreated(LocalDate.now());

        articleRepository.save(article.get());
    }

    @Override
    public List<Long> getAllArticleImageIds() {
        return articleRepository.findAll().stream().map(Article::getImage).map(Image::getId).toList();
    }

    private ArticleDTO articleMap(Article article) {
        ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);
        articleDTO.setAuthorName(article.getAuthor().getUsername());

        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
        String date = article.getCreated().format(pattern);

        articleDTO.setCreated(date);

        articleDTO.setComments(commentService.getComments(article.getId()));

        return articleDTO;
    }
}
