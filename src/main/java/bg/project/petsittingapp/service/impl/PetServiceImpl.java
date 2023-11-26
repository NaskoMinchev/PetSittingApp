package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.model.dto.AddPetBindingModel;
import bg.project.petsittingapp.model.dto.GalleryDTO;
import bg.project.petsittingapp.model.dto.PetDTO;
import bg.project.petsittingapp.model.entity.Article;
import bg.project.petsittingapp.model.entity.Image;
import bg.project.petsittingapp.model.entity.Pet;
import bg.project.petsittingapp.repository.PetRepository;
import bg.project.petsittingapp.service.ImageService;
import bg.project.petsittingapp.service.PetService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
public class PetServiceImpl implements PetService {
    private static final String FOLDER_PATH = "petPictures/";
    private final PetRepository petRepository;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    public PetServiceImpl(PetRepository petRepository, ImageService imageService, ModelMapper modelMapper) {
        this.petRepository = petRepository;
        this.imageService = imageService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addPet(AddPetBindingModel addPetBindingModel) {
        Pet pet = modelMapper.map(addPetBindingModel, Pet.class);

        MultipartFile picture = addPetBindingModel.getPicture();
        String filepath = imageService.getFilePath(FOLDER_PATH, addPetBindingModel.getName(), picture);

        Image image = imageService.createImage(picture, filepath);
        pet.setImage(image);

        petRepository.save(pet);
    }

    @Override
    public GalleryDTO getAllPets() {
        List<Pet> petsInfo = petRepository.findAll();

        GalleryDTO galleryDTO = new GalleryDTO();

        for (Pet pet : petsInfo) {
            PetDTO petDTO = modelMapper.map(pet, PetDTO.class);
            galleryDTO.getPets().add(petDTO);
        }
        Collections.reverse(galleryDTO.getPets());

        return galleryDTO;
    }

    @Override
    public List<Long> getAllPetImageIds() {
        return petRepository.findAll().stream().map(Pet::getImage).map(Image::getId).toList();
    }
}
