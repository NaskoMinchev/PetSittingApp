package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.model.dto.ArticleDTO;
import bg.project.petsittingapp.model.dto.BlogDTO;
import bg.project.petsittingapp.model.dto.CreateArticleDTO;
import bg.project.petsittingapp.model.entity.Article;
import bg.project.petsittingapp.repository.ArticleRepository;
import bg.project.petsittingapp.service.ArticleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private static final String ARTICLE_PICTURE_SAVE_PATH = "src/main/resources/static/images/articlePictures/";
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    public ArticleServiceImpl(ArticleRepository articleRepository, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(CreateArticleDTO createArticleDTO) {

        Article article = modelMapper.map(createArticleDTO, Article.class);

        MultipartFile pictureFile = createArticleDTO.getPicture();

        String filePath = getFilePath(createArticleDTO.getTitle(), pictureFile);

        createPicture(pictureFile, filePath);

        article.setPictureURL(filePath);

        articleRepository.save(article);
    }

    @Override
    public BlogDTO getAllArticles() {

        List<Article> articles = articleRepository.findAll();

        BlogDTO blogDTO = new BlogDTO();

//        for (Article article : articles) {
//            ArticleDTO articleDTO = articleMap(article);
//
//            blogDTO.getArticles().add(articleDTO);
//        }

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
            String filePath = getFilePath(articleDTO.getTitle(), pictureFile);

            createPicture(pictureFile, filePath);

            article.get().setPictureURL(filePath);
        }

        if (!articleDTO.getTitle().isEmpty()) {
            article.get().setTitle(articleDTO.getTitle());
        }

        if (!articleDTO.getBody().isEmpty()) {
            article.get().setBody(articleDTO.getBody());
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

    private static void createPicture(MultipartFile pictureFile, String filePath) {

        try {
            File file = new File(ARTICLE_PICTURE_SAVE_PATH + filePath);
            file.getParentFile().mkdirs();
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(pictureFile.getBytes());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFilePath(String title, MultipartFile picture) {
        String[] splitPictureNameArgs = picture.getOriginalFilename().split("\\.");
        String pictureFileExt = splitPictureNameArgs[splitPictureNameArgs.length - 1];
        String pathPattern = "%s_%s.%s";
        return String.format(pathPattern, transformTitle(title), UUID.randomUUID(), pictureFileExt);
    }
    private static String transformTitle(String title) {
        return title.toLowerCase().replaceAll("\\s+", "_");
    }

    private ArticleDTO articleMap(Article article) {
        ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);
        articleDTO.setAuthorName(article.getAuthor().getUsername());

        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
        String date = article.getCreated().format(pattern);

        articleDTO.setCreated(date);

        return articleDTO;
    }
}
