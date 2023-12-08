package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.model.dto.AddPetBindingModel;
import bg.project.petsittingapp.model.entity.Image;
import bg.project.petsittingapp.model.entity.Pet;
import bg.project.petsittingapp.model.enums.AnimalType;
import bg.project.petsittingapp.repository.PetRepository;
import bg.project.petsittingapp.service.ImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceImplTest {

    private PetServiceImpl petServiceToTest;
    @Mock
    private PetRepository mockPetRepository;
    @Mock
    private ImageService mockImageService;
    @Mock
    private ModelMapper mockModelMapper;

    @BeforeEach
    void setUp() {
        petServiceToTest = new PetServiceImpl(mockPetRepository, mockImageService, mockModelMapper);
    }

    @Test
    public void testAddPet() {

        AddPetBindingModel addPetBindingModel = new AddPetBindingModel();
        addPetBindingModel.setName("TestPet");
        MultipartFile mockPicture = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", new byte[0]);
        addPetBindingModel.setPicture(mockPicture);

        Pet mappedPet = new Pet();
        when(mockModelMapper.map(addPetBindingModel, Pet.class)).thenReturn(mappedPet);

        String mockFilePath = "mock/path/to/image.jpg";
        when(mockImageService.getFilePath(anyString(), anyString(), any(MultipartFile.class))).thenReturn(mockFilePath);

        Image mockImage = new Image();
        when(mockImageService.createImage(any(MultipartFile.class), eq(mockFilePath))).thenReturn(mockImage);

        petServiceToTest.addPet(addPetBindingModel);

        verify(mockModelMapper, times(1)).map(addPetBindingModel, Pet.class);
        verify(mockImageService, times(1)).getFilePath(anyString(), anyString(), any(MultipartFile.class));
        verify(mockImageService, times(1)).createImage(any(MultipartFile.class), eq(mockFilePath));
        verify(mockPetRepository, times(1)).save(mappedPet);
    }

    @Test
    void getAllPetsTest() {
        List<Pet> pets = new ArrayList<>();

        Pet pet = createPet();
        pets.add(pet);

        when(mockPetRepository.findAll()).thenReturn(pets);

        Assertions.assertEquals(pets.size(), petServiceToTest.getAllPets().getPets().size());
    }



    private static Pet createPet() {
        Pet pet = new Pet();
        pet.setName("pet");
        pet.setType(AnimalType.CAT);
        pet.setOwner("owner");

        Image image = new Image();
        image.setImageUrl("test");
        pet.setImage(image);

        return pet;
    }
}
