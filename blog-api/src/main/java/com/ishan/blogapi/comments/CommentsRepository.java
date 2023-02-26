package com.ishan.blogapi.comments;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentsRepository extends JpaRepository<CommentEntity, Integer> {

    List<CommentEntity> findByAuthorId(Integer authorId, Pageable pageable);

    List<CommentEntity> findByArticleId(Integer articleId, Pageable pageable);

    Optional<CommentEntity> findByIdAndArticleId(Integer id, Integer articleId);

    Optional<CommentEntity> findByArticleIdAndAuthorIdAndId(Integer articleId, Integer authorId, Integer commentId);
}