package bg.project.petsittingapp.model.dto;

import bg.project.petsittingapp.model.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class CreateArticleDTO {
    @NotNull
    @NotBlank
    private String title;
    @NotNull
    @NotBlank
    private String header;
    @NotNull
    @NotBlank
    private String body;
    @NotNull
    @NotBlank
    private String footer;
    @NotNull
    @NotBlank

    private MultipartFile picture;
    @NotNull
    private User author;
    @NotNull
    private LocalDate created;

    public CreateArticleDTO() {
        this.created = LocalDate.now();
    }

    public String getTitle() {
        return title;
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public String getFooter() {
        return footer;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public User getAuthor() {
        return author;
    }

    public LocalDate getCreated() {
        return created;
    }
}
