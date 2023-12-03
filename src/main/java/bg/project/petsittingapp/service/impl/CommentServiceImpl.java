package bg.project.petsittingapp.service.impl;

import bg.project.petsittingapp.model.dto.CommentDTO;
import bg.project.petsittingapp.model.dto.CreateCommentBindingModel;
import bg.project.petsittingapp.model.entity.Article;
import bg.project.petsittingapp.model.entity.Comment;
import bg.project.petsittingapp.model.entity.User;
import bg.project.petsittingapp.repository.ArticleRepository;
import bg.project.petsittingapp.repository.CommentRepository;
import bg.project.petsittingapp.repository.UserRepository;
import bg.project.petsittingapp.service.CommentService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, ArticleRepository articleRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDTO createComment(CreateCommentBindingModel createCommentBindingModel, Long id, String authorName) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Article with id " + createCommentBindingModel.getArticleId() + " not found!"));

        User user = userRepository.findByUsername(authorName)
                .orElseThrow(() -> new NoSuchElementException("User with username " + createCommentBindingModel.getAuthorName() + " not found!"));

        Comment comment = modelMapper.map(createCommentBindingModel, Comment.class);
        comment.setAuthor(user);
        comment.setArticle(article);

        commentRepository.saveAndFlush(comment);

        article.getComments().add(comment);
        articleRepository.save(article);

        return commentMap(comment);
    }

    @Transactional
    @Override
    public List<CommentDTO> getComments(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoSuchElementException("Article with id " + articleId + " not found!"));

        List<CommentDTO> comments = article.getComments().stream().map(this::commentMap).collect(Collectors.toList());
        Collections.reverse(comments);

        return comments;
    }

    private CommentDTO commentMap(Comment comment) {
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        commentDTO.setAuthorName(comment.getAuthor().getUsername());

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = comment.getPublished().format(pattern);

        commentDTO.setPublished(formattedDate);

        return commentDTO;
    }
}
