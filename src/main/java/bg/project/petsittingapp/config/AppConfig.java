package bg.project.petsittingapp.config;

import bg.project.petsittingapp.model.dto.PetDTO;
import bg.project.petsittingapp.model.entity.Image;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper
                .createTypeMap(Image.class, PetDTO.class)
                .addMappings(mapper -> mapper
                        .map(Image::getImageUrl, PetDTO::setImageUrl));

        return modelMapper;
    }
}
