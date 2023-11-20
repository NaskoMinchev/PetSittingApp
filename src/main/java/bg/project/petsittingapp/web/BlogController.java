package bg.project.petsittingapp.web;

import bg.project.petsittingapp.model.dto.BlogDTO;
import bg.project.petsittingapp.model.dto.CreateArticleDTO;
import bg.project.petsittingapp.model.entity.User;
import bg.project.petsittingapp.service.ArticleService;
import bg.project.petsittingapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
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

    @GetMapping("/blog-single/{id}")
    public ModelAndView blogSingle(@PathVariable("id") Long id) {
        return new ModelAndView("blog-single");
    }

    @GetMapping("/blog-create")
    public ModelAndView createArticle(@ModelAttribute("createArticleDTO") CreateArticleDTO createArticleDTO) {
        return new ModelAndView("blog-create");
    }

    @PostMapping("/blog-create")
    public ModelAndView createArticle(@ModelAttribute("createArticleDTO") @Valid CreateArticleDTO createArticleDTO, BindingResult bindingResult, Principal principal) {

        Optional<User> user = userService.findByUsername(principal.getName());

        if (user.isPresent()) {
            createArticleDTO.setAuthor(user.get());
        } else {
            throw new IllegalArgumentException("No such user");
        }

        if (bindingResult.hasErrors()) {
            return new ModelAndView("blog-create");
        }

        articleService.create(createArticleDTO);

        return new ModelAndView("redirect:/blog");
    }
}
