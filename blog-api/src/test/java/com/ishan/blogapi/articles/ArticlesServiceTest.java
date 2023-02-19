package com.ishan.blogapi.articles;

import com.ishan.blogapi.articles.dtos.CreateArticleDTO;
import com.ishan.blogapi.users.UserEntity;
import com.ishan.blogapi.users.UsersRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
public class ArticlesServiceTest {

    @Autowired
    private ArticlesRepository articlesRepository;

    @Mock
    private UsersRepository usersRepository;

    private ArticlesService articlesService;

    private ArticlesService getArticlesService() {
        if (articlesService == null) {
            articlesService = new ArticlesService(articlesRepository, usersRepository);
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
        getArticlesService().createArticle(1, createArticleDTO);

        var article = getArticlesService().getArticleBySlug("this-is-a-test-title");
        assertNotNull(article);
        assertEquals("This is a test title", article.getTitle());
    }

    @Test
    public void testCreateArticle() {

        CreateArticleDTO createArticleDTO = CreateArticleDTO.builder()
                .title("This is a test title")
                .body("This is a test article")
                .subtitle("This is a test subtitle")
                .build();

        when(usersRepository.findById(1)).thenReturn(Optional.of(UserEntity.builder().username("ishan").email("ishan@gmail.com").password("password").build()));
        ArticleEntity article = getArticlesService().createArticle(1, createArticleDTO);
        assertNotNull(article);
        assertEquals("This is a test title", article.getTitle());
        assertEquals("This is a test subtitle", article.getSubtitle());
        assertEquals("This is a test article", article.getBody());
        assertEquals("this-is-a-test-title", article.getSlug());
    }

    @Test
    public void testGetArticleBySlugAndExpectArticleNotFoundException() {
        ArticlesService.ArticleNotFoundException exception = assertThrows(ArticlesService.ArticleNotFoundException.class, () -> {
            getArticlesService().getArticleBySlug("this-is-a-test-title");
        });
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