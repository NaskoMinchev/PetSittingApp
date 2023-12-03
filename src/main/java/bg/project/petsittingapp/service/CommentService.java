package bg.project.petsittingapp.service;

import bg.project.petsittingapp.model.dto.CommentDTO;
import bg.project.petsittingapp.model.dto.CreateCommentBindingModel;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(CreateCommentBindingModel createCommentBindingModel, Long id, String authorName);

    List<CommentDTO> getComments(Long articleId);

}
