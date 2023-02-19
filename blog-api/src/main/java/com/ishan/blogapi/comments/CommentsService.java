package com.ishan.blogapi.comments;

import com.ishan.blogapi.articles.ArticlesRepository;
import com.ishan.blogapi.articles.ArticlesService;
import com.ishan.blogapi.comments.dtos.CreateCommentDTO;
import com.ishan.blogapi.comments.dtos.UpdateCommentDTO;
import com.ishan.blogapi.users.UsersRepository;
import com.ishan.blogapi.users.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CommentsService {
    private final CommentsRepository commentsRepository;

    private final ArticlesRepository articlesRepository;

    private final UsersRepository usersRepository;

    private final ModelMapper modelMapper;

    public CommentsService(CommentsRepository commentsRepository, ArticlesRepository articlesRepository, UsersRepository usersRepository, ModelMapper modelMapper) {
        this.commentsRepository = commentsRepository;
        this.articlesRepository = articlesRepository;
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
    }

    public CommentEntity createComment(Integer articleId, Integer userId, CreateCommentDTO createCommentDTO) {

        var article = articlesRepository.findById(articleId).orElseThrow(() -> new ArticlesService.ArticleNotFoundException(articleId));
        var user = usersRepository.findById(userId).orElseThrow(() -> new UsersService.UserNotFoundException(userId));

        CommentEntity commentEntity = CommentEntity.builder()
                .title(createCommentDTO.getTitle())
                .body(createCommentDTO.getBody())
                .article(article)
                .author(user)
                .build();

        return commentsRepository.save(commentEntity);
    }

    public CommentEntity updateComment(Integer articleId, Integer userId, Integer commentId, UpdateCommentDTO updateCommentDTO) {
        var commentEntity = commentsRepository.findByArticleIdAndAuthorIdAndId(articleId, userId, commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        if (updateCommentDTO.getTitle() != null) {
            commentEntity.setTitle(updateCommentDTO.getTitle());
        }
        if (updateCommentDTO.getBody() != null) {
            commentEntity.setBody(updateCommentDTO.getBody());
        }
        return commentsRepository.save(commentEntity);
    }

    public static class CommentNotFoundException extends IllegalArgumentException {
        public CommentNotFoundException(Integer commentId) {
            super("Comment with id: " + commentId + " not found");
        }
    }
}