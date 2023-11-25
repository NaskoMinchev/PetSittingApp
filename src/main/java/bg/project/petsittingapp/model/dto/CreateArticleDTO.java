package bg.project.petsittingapp.model.dto;

import bg.project.petsittingapp.model.entity.User;
import bg.project.petsittingapp.validation.ImageAnnotation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class CreateArticleDTO {
    @NotNull(message = "Enter title")
    @NotBlank(message = "Title can not be blank!")
    private String title;
    @NotNull(message = "Enter header")
    @NotBlank(message = "Header can not be blank!")
    private String header;
    @NotNull(message = "Enter body!")
    @NotBlank(message = "Body can not be blank!")
    private String body;
    @NotNull(message = "Enter footer!")
    @NotBlank(message = "Footer can not be blank!")
    private String footer;
    @ImageAnnotation(contentTypes = {"image/jpeg", "image/png"})
    private MultipartFile picture;
    private User author;
    @NotNull
    private LocalDate created;

    public CreateArticleDTO() {
        this.created = LocalDate.now();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }
}
