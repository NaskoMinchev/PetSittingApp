package bg.project.petsittingapp.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ContactFormDTO {
    @NotNull
    @NotBlank(message = "Please enter your name!")
    private String name;
    @NotNull
    @Email(message = "Email must be valid!")
    @NotBlank(message = "Email cannot be empty!")
    private String email;
    @NotNull
    @NotBlank(message = "Please enter a subject!")
    private String subject;
    @NotNull
    @NotBlank(message = "Please write a message!")
    private String message;

    public ContactFormDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
