package com.example.springtaskmgrv2.entities;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;


@Entity(name = "notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteEntity extends BaseEntity {

    @Column(name = "body", nullable = false, length = 500)
    String body;

    @ManyToOne
    TaskEntity task;
}
