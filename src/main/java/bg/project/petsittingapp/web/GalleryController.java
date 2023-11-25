package bg.project.petsittingapp.web;

import bg.project.petsittingapp.model.dto.AddPetBindingModel;
import bg.project.petsittingapp.model.dto.GalleryDTO;
import bg.project.petsittingapp.service.PetService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GalleryController {

    private final PetService petService;

    public GalleryController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/gallery")
    public ModelAndView gallery() {

        GalleryDTO galleryDTO = petService.getAllPets();

        ModelAndView modelAndView = new ModelAndView("gallery");
        modelAndView.addObject(galleryDTO);

        return modelAndView;
    }

    @GetMapping("/pet/add")
    public ModelAndView addPet(@ModelAttribute("addPetBindingModel") AddPetBindingModel addPetBindingModel) {
        return new ModelAndView("pet-add");
    }

    @PostMapping("/pet/add")
    public ModelAndView addPet(@ModelAttribute("addPetBindingModel")
                                   @Valid AddPetBindingModel addPetBindingModel,
                                    BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addPetBindingModel", addPetBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addPetBindingModel", bindingResult);
            return new ModelAndView("pet-add");
        }
        petService.addPet(addPetBindingModel);

        return new ModelAndView("redirect:/gallery");
    }
}
