package com.ishan.blogapi.articles;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticlesRepository extends JpaRepository<ArticleEntity, Integer> {
    Optional<ArticleEntity> findBySlug(String slug);
    List<ArticleEntity> findByAuthorId(Integer authorId, Pageable pageable);

    List<ArticleEntity> findByTagListContaining(String tag, Pageable pageable);
}