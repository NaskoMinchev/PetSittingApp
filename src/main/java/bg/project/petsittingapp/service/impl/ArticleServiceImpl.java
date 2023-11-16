package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.model.dto.CreateArticleDTO;
import bg.project.petsittingapp.repository.ArticleRepository;
import bg.project.petsittingapp.service.ArticleService;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public void Create(CreateArticleDTO createArticleDTO) {

    }
}
