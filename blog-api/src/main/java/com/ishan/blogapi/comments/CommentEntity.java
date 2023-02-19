package com.ishan.blogapi.comments;

import com.ishan.blogapi.articles.ArticleEntity;
import com.ishan.blogapi.commons.BaseEntity;
import com.ishan.blogapi.users.UserEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity(name = "comments")
public class CommentEntity extends BaseEntity {

    @Column(nullable = false, length = 100)
    String title;
    @Column(length = 1000)
    String body;

    @ManyToOne // means that this is a many-to-one relationship with the UserEntity
    // that means that a single user can have many comments
    UserEntity author;

    @ManyToOne // means that this is a many-to-one relationship with the ArticleEntity
    // that means that a single article can have many comments
    ArticleEntity article;

}
