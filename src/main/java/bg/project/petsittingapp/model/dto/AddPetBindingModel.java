package bg.project.petsittingapp.model.dto;

import bg.project.petsittingapp.model.enums.AnimalType;
import bg.project.petsittingapp.validation.ImageAnnotation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class AddPetBindingModel {
    @NotNull(message = "Pet name cannot be null!")
    @NotBlank(message = "Pet name cannot be empty!")
    @Size(min = 1, message = "Pet name length must be at least 1 character!")
    private String name;
    private String breed;
    @NotNull(message = "Owner name cannot be null!")
    @NotBlank(message = "Owner name cannot be empty!")
    @Size(min = 1, message = "Owner name length must be at least 1 character!")
    private String owner;
    @NotNull(message = "Select animal type!")
    private AnimalType type;
    @ImageAnnotation(contentTypes = {"image/jpeg", "image/png"})
    private MultipartFile picture;
    public AddPetBindingModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public AnimalType getType() {
        return type;
    }

    public void setType(AnimalType type) {
        this.type = type;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }

}
