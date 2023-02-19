package com.ishan.blogapi.users;

import com.ishan.blogapi.articles.ArticleEntity;
import com.ishan.blogapi.commons.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "users")
@Setter
@Getter
public class UserEntity extends BaseEntity {

    @Column(unique = true, nullable = false, length = 50)
    String username;
    String password; // TODO: Hash this
    String email;
    String bio;
    String image;

    @ManyToMany(mappedBy = "likedBy")
    // means that this is a many-to-many relationship with the ArticleEntity
    // that means that a single user can like many articles
    // and a single article can be liked by many users
    List<ArticleEntity> likedArticles;

    @ManyToMany
    @JoinTable(
            name = "user_follows",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    // means that this is a many-to-many relationship with the UserEntity
    // that means that a single user can follow many users
    // and a single user can be followed by many users
    List<UserEntity> following;

    @ManyToMany(mappedBy = "following")
    // means that this is a many-to-many relationship with the UserEntity
    // that means that a single user can follow many users
    // and a single user can be followed by many users
    List<UserEntity> followers;

}
