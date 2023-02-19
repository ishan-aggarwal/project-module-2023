package com.ishan.blogapi.articles;

import com.ishan.blogapi.articles.dtos.CreateArticleDTO;
import com.ishan.blogapi.articles.dtos.UpdateArticleDTO;
import com.ishan.blogapi.users.UsersRepository;
import com.ishan.blogapi.users.UsersService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ArticlesService {
    private final ArticlesRepository articlesRepository;
    private final UsersRepository usersRepository;

    public ArticlesService(ArticlesRepository articlesRepository, UsersRepository usersRepository) {
        this.articlesRepository = articlesRepository;
        this.usersRepository = usersRepository;
    }

    public List<ArticleEntity> getAllArticles() {
        return articlesRepository.findAll();
    }

    public ArticleEntity getArticleBySlug(String slug) {
        ArticleEntity articleEntity = articlesRepository.findBySlug(slug).orElseThrow(() -> new ArticleNotFoundException(slug));
        return articleEntity;
    }

    public ArticleEntity createArticle(Integer authorId, CreateArticleDTO createArticleDTO) {
        var author = usersRepository.findById(authorId).orElseThrow(() -> new UsersService.UserNotFoundException(authorId));
        ArticleEntity articleEntity = articlesRepository.save(ArticleEntity.builder()
                .title(createArticleDTO.getTitle())
                .slug(createArticleDTO.getTitle().toLowerCase().replaceAll("\\s+", "-"))
                .body(createArticleDTO.getBody())
                .subtitle(createArticleDTO.getSubtitle())
                .author(author)
                .tagList(createArticleDTO.getTagList())
                .build()
        );
        return articleEntity;
    }

    public ArticleEntity updateArticle(Integer authorId, Integer articleId, UpdateArticleDTO updateArticleDTO) {
        var article = articlesRepository.findByAuthorIdAndId(authorId, articleId).orElseThrow(() -> new ArticleNotFoundException(articleId));

        if (updateArticleDTO.getTitle() != null) {
            article.setTitle(updateArticleDTO.getTitle());
            article.setSlug(updateArticleDTO.getTitle().toLowerCase().replaceAll("\\s+", "-"));
        }

        if (updateArticleDTO.getBody() != null) {
            article.setBody(updateArticleDTO.getBody());
        }

        if (updateArticleDTO.getSubtitle() != null) {
            article.setSubtitle(updateArticleDTO.getSubtitle());
        }

        if (!CollectionUtils.isEmpty(updateArticleDTO.getTagList())) {
            article.setTagList(updateArticleDTO.getTagList());
        }

        return articlesRepository.save(article);
    }


    public static class ArticleNotFoundException extends IllegalArgumentException {
        public ArticleNotFoundException(String slug) {
            super("Article " + slug + " not found");
        }

        public ArticleNotFoundException(Integer id) {
            super("Article with id: " + id + " not found");
        }
    }
}