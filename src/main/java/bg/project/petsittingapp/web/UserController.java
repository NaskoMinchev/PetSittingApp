package bg.project.petsittingapp.web;

import bg.project.petsittingapp.model.dto.UserRegisterBindingModel;
import bg.project.petsittingapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public ModelAndView register(@ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel) {
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute ("userRegisterBindingModel")
                                     @Valid UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("redirect:/register");
        }

        boolean hasPasswordsMatch = userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword());

        if (!hasPasswordsMatch) {
            ModelAndView modelAndView = new ModelAndView("redirect:/register");
            modelAndView.addObject("passwordsNotMatch", true);
            return modelAndView;
        }

        boolean registrationSuccessful = userService.register(userRegisterBindingModel);

        if (!registrationSuccessful) {
            return new ModelAndView("redirect:/register");
        }

        return new ModelAndView("redirect:/login");
    }
}
