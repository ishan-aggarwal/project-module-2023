package com.ishan.blogapi.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentsRepository extends JpaRepository<CommentEntity, Integer> {
    Optional<CommentEntity> findByArticleIdAndAuthorIdAndId(Integer articleId, Integer authorId, Integer commentId);
}