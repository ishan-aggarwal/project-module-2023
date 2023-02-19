package com.ishan.blogapi.commons;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass // means that this class will not be mapped to a table
@Getter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    // means that the id will be generated automatically
    // and will be a sequence
    Integer id;

    @CreatedDate()
    @Column(name = "created_at", updatable = false)
    // means that the date will be automatically generated
    // and will not be updated
    Date createdAt;

}
