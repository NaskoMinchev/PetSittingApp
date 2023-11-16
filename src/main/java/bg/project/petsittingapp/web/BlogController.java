package bg.project.petsittingapp.web;

import bg.project.petsittingapp.model.dto.CreateArticleDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BlogController {

    @GetMapping("/blog")
    public ModelAndView blog() {
        return new ModelAndView("blog");
    }

    @GetMapping("/blog-single")
    public ModelAndView blogSingle() {
        return new ModelAndView("blog-single");
    }

    @GetMapping("/blog-create")
    public ModelAndView createArticle(@ModelAttribute("createArticleDTO") CreateArticleDTO createArticleDTO) {
        return new ModelAndView("blog-create");
    }

    @PostMapping("/blog-create")
    public ModelAndView createArticle(@ModelAttribute("createArticleDTO") @Valid CreateArticleDTO createArticleDTO, BindingResult bindingResult) {
        return new ModelAndView("blog-create");
    }
}
