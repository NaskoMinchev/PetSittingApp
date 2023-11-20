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
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

        try {
            File file = new File(ARTICLE_PICTURE_SAVE_PATH + filePath);
            file.getParentFile().mkdirs();
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(createArticleDTO.getPicture().getBytes());

            article.setPictureURL(filePath);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        articleRepository.save(article);

    }

    @Override
    public BlogDTO getAllArticles() {

        List<Article> articles = articleRepository.findAll();

        BlogDTO blogDTO = new BlogDTO();

        for (Article article : articles) {
            ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);
            articleDTO.setAuthorName(article.getAuthor().getUsername());

            DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
            String date = article.getCreated().format(pattern);

            articleDTO.setCreated(date);

            blogDTO.getArticles().add(articleDTO);
        }

        Collections.reverse(blogDTO.getArticles());

        return blogDTO;
    }

    private String getFilePath(String title, MultipartFile picture) {
        String[] splitPictureNameArgs = picture.getOriginalFilename().split("\\.");
        String pictureFileExt = splitPictureNameArgs[splitPictureNameArgs.length - 1];
        String pathPattern = "%s_%s.%s";
        return String.format(pathPattern, transformTitle(title), UUID.randomUUID(), pictureFileExt);
    }
    private String transformTitle(String title) {
        return title.toLowerCase().replaceAll("\\s+", "_");
    }
}
