package bg.project.petsittingapp.service;

import bg.project.petsittingapp.model.dto.AddPetBindingModel;
import bg.project.petsittingapp.model.dto.GalleryDTO;

import java.util.List;

public interface PetService {
    void addPet(AddPetBindingModel addPetBindingModel);
    GalleryDTO getAllPets();
    List<Long> getAllPetImageIds();
}
