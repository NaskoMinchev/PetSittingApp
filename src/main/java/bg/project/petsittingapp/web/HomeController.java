package bg.project.petsittingapp.web;

import bg.project.petsittingapp.model.dto.BlogDTO;
import bg.project.petsittingapp.model.dto.GalleryDTO;
import bg.project.petsittingapp.service.ArticleService;
import bg.project.petsittingapp.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    private final ArticleService articleService;
    private final PetService petService;

    public HomeController(ArticleService articleService, PetService petService) {
        this.articleService = articleService;
        this.petService = petService;
    }

    @GetMapping("/")
    public ModelAndView index() {

        BlogDTO blogDTO = articleService.getAllArticles();
        GalleryDTO galleryDTO = petService.getAllPets();

        if (blogDTO.getArticles().size() >= 3) {
            blogDTO.setArticles(blogDTO.getArticles().subList(0, 3));
        }

        if (galleryDTO.getPets().size() >= 3) {
            galleryDTO.setPets(galleryDTO.getPets().subList(0, 3));
        }

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject(blogDTO);
        modelAndView.addObject(galleryDTO);

        return modelAndView;
    }
    @GetMapping("/about")
    public ModelAndView about() {
        return new ModelAndView("about");
    }

    @GetMapping("/services")
    public ModelAndView cervices() {
        return new ModelAndView("services");
    }
}
