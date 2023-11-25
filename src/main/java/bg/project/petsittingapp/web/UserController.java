package bg.project.petsittingapp.web;

import bg.project.petsittingapp.model.dto.UserDTO;
import bg.project.petsittingapp.model.dto.UserRegisterBindingModel;
import bg.project.petsittingapp.model.entity.User;
import bg.project.petsittingapp.model.entity.UserRole;
import bg.project.petsittingapp.service.UserRoleService;
import bg.project.petsittingapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final UserRoleService userRoleService;

    public UserController(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @GetMapping("/register")
    public ModelAndView register(@ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel) {
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute ("userRegisterBindingModel")
                                     @Valid UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }

        boolean hasPasswordsMatch = userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword());

        if (!hasPasswordsMatch) {
            ModelAndView modelAndView = new ModelAndView("register");
            modelAndView.addObject("passwordsNotMatch", true);
            return modelAndView;
        }

        boolean registrationSuccessful = userService.register(userRegisterBindingModel);

        if (!registrationSuccessful) {
            return new ModelAndView("register");
        }

        return new ModelAndView("redirect:/login");
    }
    @GetMapping("/user/all")
    public ModelAndView getAllUsers() {
        ModelAndView modelAndView = new ModelAndView("users-view");

        List<User> users = userService.findAllUsers();
        modelAndView.addObject("users", users);

        return modelAndView;
    }

    @GetMapping("/user/update/{id}")
    public ModelAndView editUser(@PathVariable("id") Long id) {

        ModelAndView modelAndView = new ModelAndView("user-edit");
        UserDTO userDTO = userService.findById(id);
        List<UserRole> roles = userRoleService.findAll();

        if (userDTO != null) {
            modelAndView.addObject(userDTO);
            modelAndView.addObject("userRoles", roles);
        } else {
            throw new IllegalArgumentException("No such user!");
        }

        return modelAndView;
    }

    @PostMapping("/user/edit/save/{id}")
    public ModelAndView saveEditedUser(@ModelAttribute("user") UserDTO userDTO) {

        userService.saveEditedUser(userDTO);

        return new ModelAndView("redirect:/user/all");
    }

    @DeleteMapping("/user/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") Long id) {

        userService.deleteUser(id);

        return new ModelAndView("redirect:/user/all");
    }
}
