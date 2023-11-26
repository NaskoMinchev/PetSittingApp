package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.repository.CommentRepository;
import bg.project.petsittingapp.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
}
