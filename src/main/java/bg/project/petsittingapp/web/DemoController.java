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

    @GetMapping("/contact")
    public ModelAndView contact() {
        return new ModelAndView("contact");
    }

    @GetMapping("/gallery")
    public ModelAndView gallery() {
        return new ModelAndView("gallery");
    }

    @GetMapping("/pricing")
    public ModelAndView pricing() {
        return new ModelAndView("pricing");
    }

    @GetMapping("/services")
    public ModelAndView cervices() {
        return new ModelAndView("services");
    }

    @GetMapping("/vet")
    public ModelAndView vet() {
        return new ModelAndView("vet");
    }
}