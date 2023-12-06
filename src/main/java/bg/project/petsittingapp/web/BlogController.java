package bg.project.petsittingapp.web;

import bg.project.petsittingapp.model.dto.ArticleDTO;
import bg.project.petsittingapp.model.dto.BlogDTO;
import bg.project.petsittingapp.model.dto.CreateArticleDTO;
import bg.project.petsittingapp.model.entity.User;
import bg.project.petsittingapp.service.ArticleService;
import bg.project.petsittingapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class BlogController {
    private final ArticleService articleService;
    private final UserService userService;

    public BlogController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping("/blog")
    public ModelAndView blog() {

        BlogDTO blogDTO = articleService.getAllArticles();

        ModelAndView modelAndView = new ModelAndView("blog");
        modelAndView.addObject(blogDTO);

        return modelAndView;
    }

    @GetMapping("/blog/article/{id}")
    public ModelAndView blogSingle(@PathVariable("id") Long id) {

        ArticleDTO articleDTO = articleService.getSingleArticle(id);

        BlogDTO blogDTO = articleService.getAllArticles();

        if (blogDTO.getArticles().size() >= 3) {
            blogDTO.setArticles(blogDTO.getArticles().subList(0, 3));
        }

        ModelAndView modelAndView = new ModelAndView("blog-single");
        modelAndView.addObject(articleDTO);
        modelAndView.addObject(blogDTO);

        return modelAndView;
    }

    @GetMapping("/blog/create")
    public ModelAndView createArticle(@ModelAttribute("createArticleDTO") CreateArticleDTO createArticleDTO) {
        return new ModelAndView("blog-create");
    }

    @PostMapping("/blog/create")
    public ModelAndView createArticle(@ModelAttribute("createArticleDTO") @Valid CreateArticleDTO createArticleDTO,
                                      BindingResult bindingResult, Principal principal) {

        createArticleDTO.setAuthor(principal.getName());

        if (bindingResult.hasErrors()) {
            return new ModelAndView("blog-create");
        }

        articleService.create(createArticleDTO);

        return new ModelAndView("redirect:/blog");
    }

    @DeleteMapping("/blog/article/delete/{id}")
    public ModelAndView deleteArticle(@PathVariable("id") Long id) {

        articleService.deleteArticle(id);

        return new ModelAndView("redirect:/blog");
    }

    @GetMapping("/blog/article/edit/{id}")
    public ModelAndView editArticle(@PathVariable("id") Long id) {

        ArticleDTO articleDTO = articleService.getSingleArticle(id);

        ModelAndView modelAndView = new ModelAndView("blog-edit");
        modelAndView.addObject(articleDTO);

        return modelAndView;
    }

    @PostMapping("/blog/article/edit/save/{id}")
    public ModelAndView saveEditedArticle(@ModelAttribute("articleDTO") ArticleDTO articleDTO) {

        articleService.editArticle(articleDTO);

        return new ModelAndView("redirect:/blog");
    }
}
