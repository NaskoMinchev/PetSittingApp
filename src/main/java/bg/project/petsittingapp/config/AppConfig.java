package bg.project.petsittingapp.config;

import bg.project.petsittingapp.model.dto.ArticleDTO;
import bg.project.petsittingapp.model.dto.PetDTO;
import bg.project.petsittingapp.model.entity.Article;
import bg.project.petsittingapp.model.entity.Image;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static java.time.format.DateTimeFormatter.ofPattern;

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
