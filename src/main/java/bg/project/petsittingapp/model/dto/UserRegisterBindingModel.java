package bg.project.petsittingapp.model.dto;

import bg.project.petsittingapp.validation.UniqueEmail;
import bg.project.petsittingapp.validation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRegisterBindingModel {
    @NotNull
    @UniqueUsername(message = "Username already taken!")
    @Size(min = 4, max = 20, message = "Username length must be between 4 and 20 characters!")
    @NotBlank(message = "Username length must be between 4 and 20 characters!")
    private String username;

    @Email
    @NotNull
    @UniqueEmail(message = "Email already taken!")
    @NotBlank(message = "Email cannot be empty!")
    private String email;

    @NotNull
    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters!")
    @NotBlank(message = "Password length must be between 3 and 20 characters!")
    private String password;

    @NotNull
    @Size(min = 3, max = 20)
    @NotBlank
    private String confirmPassword;

    public UserRegisterBindingModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
