package bg.project.petsittingapp.web;

import bg.project.petsittingapp.model.dto.BlogDTO;
import bg.project.petsittingapp.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    private final ArticleService articleService;

    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public ModelAndView index() {

        BlogDTO blogDTO = articleService.getAllArticles();
        blogDTO.setArticles(blogDTO.getArticles().subList(0, 3));

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject(blogDTO);

        return modelAndView;
    }
}
