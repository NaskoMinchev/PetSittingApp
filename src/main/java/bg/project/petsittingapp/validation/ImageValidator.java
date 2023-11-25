package bg.project.petsittingapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ImageValidator implements ConstraintValidator<ImageAnnotation, MultipartFile> {

    private List<String> contentTypes;
    private long size;

    @Override
    public void initialize(ImageAnnotation constraintAnnotation) {
        this.contentTypes = Arrays.stream(constraintAnnotation.contentTypes()).toList();
        this.size = constraintAnnotation.size();

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

        String errorMessage = getErrorMessage(file);

        if (!errorMessage.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
            return false;
        }
        return true;
    }

    private String getErrorMessage(MultipartFile file) {

        if (file.isEmpty()) {
            return "File must be provided";
        }

        if (file.getSize() > size) {
            return "Exceeded file size! Max file size: " + size;
        }

        if (!contentTypes.contains(file.getContentType())) {
            return "Invalid file extension! Supported files: " + String.join(", ", contentTypes);
        }

        return "";
    }
}
