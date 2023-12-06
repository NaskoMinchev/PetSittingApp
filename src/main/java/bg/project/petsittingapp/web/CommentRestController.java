package bg.project.petsittingapp.web;

import bg.project.petsittingapp.model.dto.CommentDTO;
import bg.project.petsittingapp.model.dto.CreateCommentBindingModel;
import bg.project.petsittingapp.service.CommentService;
import bg.project.petsittingapp.validation.ApiError;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
public class CommentRestController {

    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/blog/article/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable("id") Long id, Principal principal) {
        return ResponseEntity.ok(
                commentService.getComments(id));
    }

    @PostMapping("/blog/article/{id}/comments")
    public ResponseEntity<CommentDTO> newComment(Principal principal,
                                                 @PathVariable("id") Long id,
                                                 @RequestBody @Valid CreateCommentBindingModel createCommentBindingModel) {

        createCommentBindingModel.setAuthorName(principal.getName());
        CommentDTO commentDTO = commentService.createComment(createCommentBindingModel, id);
        commentDTO.setAuthorName(principal.getName());
        commentDTO.setArticleId(id);

        URI locationOfNewComment =
                URI.create(String.format("/blog/article/%d/comments/%d", id, commentDTO.getId()));

        return ResponseEntity.
                created(locationOfNewComment).
                body(commentDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> onValidationFailure(MethodArgumentNotValidException exc) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        exc.getFieldErrors().forEach(fe ->
                apiError.addFieldWithError(fe.getField()));

        return ResponseEntity.badRequest().body(apiError);
    }
}
