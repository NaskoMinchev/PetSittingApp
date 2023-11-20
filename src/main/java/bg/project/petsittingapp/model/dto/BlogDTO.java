package bg.project.petsittingapp.model.dto;

import java.util.ArrayList;
import java.util.List;

public class BlogDTO {
    private List<ArticleDTO> articles;

    public BlogDTO() {
        this.articles = new ArrayList<>();
    }

    public List<ArticleDTO> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleDTO> articles) {
        this.articles = articles;
    }
}
