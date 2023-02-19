package com.ishan.blogapi.comments;

import com.ishan.blogapi.articles.ArticleEntity;
import com.ishan.blogapi.articles.ArticlesRepository;
import com.ishan.blogapi.articles.ArticlesService;
import com.ishan.blogapi.comments.dtos.CreateCommentDTO;
import com.ishan.blogapi.users.UserEntity;
import com.ishan.blogapi.users.UsersRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.ui.ModelMap;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@DataJpaTest
public class CommentsServiceTest {

    @Autowired
    private CommentsRepository commentsRepository;
    private CommentsService commentsService;
    @Mock
    private ArticlesRepository articlesRepository;
    @Mock
    private UsersRepository usersRepository;

    public CommentsService getCommentsService() {
        if (commentsService == null) {
            var modelMapper = new ModelMapper();
            commentsService = new CommentsService(commentsRepository, articlesRepository, usersRepository, modelMapper);
        }
        return commentsService;
    }

    @Test
    public void testCreateComment() {
        var articleId = 1;
        var userId = 1;
        var createCommentDTO = CreateCommentDTO.builder().title("title").body("body").build();
        when(articlesRepository.findById(articleId)).thenReturn(Optional.of(ArticleEntity.builder().title("title").subtitle("sub-title").body("body").build()));
        when(usersRepository.findById(userId)).thenReturn(Optional.of(UserEntity.builder().username("ishan").email("ishan@gmail.com").password("password").build()));
        CommentEntity commentEntity = getCommentsService().createComment(articleId, userId, createCommentDTO);
        assertNotNull(commentEntity);
    }

}
