package bg.project.petsittingapp.service;

import bg.project.petsittingapp.model.dto.BlogDTO;
import bg.project.petsittingapp.model.dto.CreateArticleDTO;

public interface ArticleService {

    void create(CreateArticleDTO createArticleDTO);

    BlogDTO getAllArticles();
}
