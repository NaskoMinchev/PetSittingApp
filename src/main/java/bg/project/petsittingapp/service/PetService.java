package bg.project.petsittingapp.service;

import bg.project.petsittingapp.model.dto.AddPetBindingModel;
import bg.project.petsittingapp.model.dto.GalleryDTO;

public interface PetService {
    void addPet(AddPetBindingModel addPetBindingModel);
    GalleryDTO getAllPets();
}
