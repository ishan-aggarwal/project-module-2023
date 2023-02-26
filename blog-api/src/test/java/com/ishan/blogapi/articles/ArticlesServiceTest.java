package com.ishan.blogapi.articles;

import com.ishan.blogapi.articles.dtos.ArticleResponseDTO;
import com.ishan.blogapi.articles.dtos.CreateArticleDTO;
import com.ishan.blogapi.comments.CommentsRepository;
import com.ishan.blogapi.users.UserEntity;
import com.ishan.blogapi.users.UsersRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
public class ArticlesServiceTest {

    @Autowired
    private ArticlesRepository articlesRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private CommentsRepository commentsRepository;

    private ArticlesService articlesService;

    private ArticlesService getArticlesService() {
        if (articlesService == null) {
            var modelMapper = new ModelMapper();
            articlesService = new ArticlesService(articlesRepository, usersRepository, commentsRepository, modelMapper);
        }
        return articlesService;
    }

    @Test
    public void getAllArticles() {
        var articles = getArticlesService().getAllArticles();
        assertNotNull(articles);
    }

    @Test
    public void testGetArticleBySlug() {

        CreateArticleDTO createArticleDTO = CreateArticleDTO.builder()
                .title("This is a test title")
                .body("This is a test article")
                .subtitle("This is a test subtitle")
                .build();

        when(usersRepository.findById(1)).thenReturn(Optional.of(UserEntity.builder().username("ishan").email("ishan@gmail.com").password("password").build()));
        ArticleResponseDTO articleResponseDTO = getArticlesService().createArticle(1, createArticleDTO);

        var article = getArticlesService().getArticleBySlug(articleResponseDTO.getSlug());
        assertNotNull(article);
        assertEquals("This is a test title", article.getTitle());
    }

    @Test
    public void testCreateArticle() {

        CreateArticleDTO createArticleDTO = CreateArticleDTO.builder()
                .title("This is a test title")
                .body("This is a test article")
                .subtitle("This is a test subtitle")
                .tagList(List.of("tag1", "tag2"))
                .build();

        when(usersRepository.findById(1)).thenReturn(Optional.of(UserEntity.builder().username("ishan").email("ishan@gmail.com").password("password").build()));
        ArticleResponseDTO article = getArticlesService().createArticle(1, createArticleDTO);
        assertNotNull(article);
        assertEquals("This is a test title", article.getTitle());
        assertEquals("This is a test subtitle", article.getSubtitle());
        assertEquals("This is a test article", article.getBody());
        assertTrue(article.getSlug().startsWith("this-is-a-test-title"));
        assertEquals("ishan", article.getUsername());
        assertEquals(2, article.getTagList().size());
        assertEquals("ishan@gmail.com", article.getEmail());
    }

    @Test
    public void testGetArticleBySlugAndExpectArticleNotFoundException() {
        ArticlesService.ArticleNotFoundException exception = assertThrows(ArticlesService.ArticleNotFoundException.class, () -> getArticlesService().getArticleBySlug("this-is-a-test-title"));
        assertNotNull(exception);
        assertEquals("Article this-is-a-test-title not found", exception.getMessage());
    }

//    @Test
//    public void testUpdateArticle() {
//        CreateArticleDTO createArticleDTO = CreateArticleDTO.builder()
//                .title("This is a test title")
//                .body("This is a test article")
//                .subtitle("This is a test subtitle")
//                .build();
//
//        when(usersRepository.findById(1)).thenReturn(Optional.of(UserEntity.builder().username("ishan").email("ishan@gmail.com").password("password").build()));
//        getArticlesService().createArticle(1, createArticleDTO);
//
//        UpdateArticleDTO updateArticleDTO = UpdateArticleDTO.builder()
//                .title("This is a test title1")
//                .build();
//
//        ArticleEntity updateArticle = getArticlesService().updateArticle(1, 1, updateArticleDTO);
//        assertNotNull(updateArticle);
//        assertEquals("This is a test title1", updateArticle.getTitle());
//        assertEquals("This is a test subtitle", updateArticle.getSubtitle());
//    }
}