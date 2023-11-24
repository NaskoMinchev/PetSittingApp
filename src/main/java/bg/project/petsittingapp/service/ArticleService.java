package bg.project.petsittingapp.service;

import bg.project.petsittingapp.model.dto.ArticleDTO;
import bg.project.petsittingapp.model.dto.BlogDTO;
import bg.project.petsittingapp.model.dto.CreateArticleDTO;

import java.awt.print.Pageable;

public interface ArticleService {

    void create(CreateArticleDTO createArticleDTO);

    BlogDTO getAllArticles();

    ArticleDTO getSingleArticle(Long id);

    void deleteArticle(Long id);

    void editArticle(ArticleDTO articleDTO);
}
