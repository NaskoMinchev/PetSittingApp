package bg.project.petsittingapp.service;

import bg.project.petsittingapp.model.dto.CommentDTO;
import bg.project.petsittingapp.model.dto.CreateCommentBindingModel;
import bg.project.petsittingapp.model.entity.Comment;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(CreateCommentBindingModel createCommentBindingModel, Long id);

    List<CommentDTO> getComments(Long articleId);
    void deleteAllArticleComments(Long id);

}
