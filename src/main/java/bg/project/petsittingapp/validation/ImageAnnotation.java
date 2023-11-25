package bg.project.petsittingapp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageValidator.class)
public @interface ImageAnnotation {
    long size() default 5 *1024 * 1024;
    String[] contentTypes();

    String message() default "Incorrect file!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
