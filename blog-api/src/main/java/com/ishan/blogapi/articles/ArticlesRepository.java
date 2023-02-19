package com.ishan.blogapi.articles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticlesRepository extends JpaRepository<ArticleEntity, Integer> {
    Optional<ArticleEntity> findBySlug(String slug);

    Optional<ArticleEntity> findByAuthorId(Integer authorId);

    Optional<ArticleEntity> findByAuthorIdAndId(Integer authorId, Integer articleId);
}