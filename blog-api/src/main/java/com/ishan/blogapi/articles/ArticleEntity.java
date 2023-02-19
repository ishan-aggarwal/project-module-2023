package com.ishan.blogapi.articles;

import com.ishan.blogapi.commons.BaseEntity;
import com.ishan.blogapi.users.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "articles")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleEntity extends BaseEntity {

    @Column(unique = true, nullable = false, length = 150)
    String slug;
    @Column(nullable = false, length = 200)
    String title;
    String subtitle;
    @Column(nullable = false, length = 8000)
    String body;

    @ElementCollection
    // means that this is a collection of elements that are not entities themselves but are embedded in the entity that contains
    // them as a value type or as a collection of value types (i.e. a collection of strings) and that are mapped to a table
    // that contains a column for each element of the collection and a foreign key column that references the primary key of the
    // entity that contains the collection of elements (i.e. the article table)
    private List<String> tagList; // TODO: Implement this

    @ManyToOne(cascade = CascadeType.ALL)
    // means that this is a many-to-one relationship with the UserEntity
    // that means that a single user can have many articles
    UserEntity author;

    @ManyToMany
    // JoinTable is used to specify the name of the join table
    // and the names of the columns in the join table
    // that reference the primary keys of the two tables
    // that are being joined together in the many-to-many relationship

    // Note: The name of the join table is article_likes
    // The name of the column that references the primary key of the article table is article_id
    // The name of the column that references the primary key of the user table is user_id

    // Also note that it is not necessary to make use of JoinTable annotation
    @JoinTable(
            name = "article_likes",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    // means that this is a many-to-many relationship with the UserEntity
    // that means that a single user can like many articles
    // and a single article can be liked by many users
    List<UserEntity> likedBy;
}
