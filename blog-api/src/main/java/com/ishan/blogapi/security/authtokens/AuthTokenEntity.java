package com.ishan.blogapi.security.authtokens;

import com.ishan.blogapi.users.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "auth_tokens")
@Getter
@Setter
public class AuthTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "VARCHAR(256)", updatable = false, nullable = false)
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne
    private UserEntity user;
}