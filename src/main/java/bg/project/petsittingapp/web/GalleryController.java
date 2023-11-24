package bg.project.petsittingapp.web;

import bg.project.petsittingapp.model.dto.AddPetBindingModel;
import bg.project.petsittingapp.model.dto.GalleryDTO;
import bg.project.petsittingapp.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView addPet(@ModelAttribute("addPetBindingModel") AddPetBindingModel addPetBindingModel, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("pet-add");
        }
        petService.addPet(addPetBindingModel);

        return new ModelAndView("redirect:/gallery");
    }
}
