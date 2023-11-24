package bg.project.petsittingapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DemoController {


    @GetMapping("/about")
    public ModelAndView about() {
        return new ModelAndView("about");
    }




    @GetMapping("/services")
    public ModelAndView cervices() {
        return new ModelAndView("services");
    }
}
