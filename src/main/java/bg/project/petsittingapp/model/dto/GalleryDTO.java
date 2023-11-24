package bg.project.petsittingapp.model.dto;

import java.util.ArrayList;
import java.util.List;

public class GalleryDTO {
    private List<PetDTO> pets;

    public GalleryDTO() {
        this.pets = new ArrayList<>();
    }

    public List<PetDTO> getPets() {
        return pets;
    }

    public void setPets(List<PetDTO> pets) {
        this.pets = pets;
    }
}
