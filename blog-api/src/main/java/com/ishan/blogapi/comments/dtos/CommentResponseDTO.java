package com.ishan.blogapi.comments.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentResponseDTO {
    private Integer id;
    private String title;
    private String body;
    private String articleSlug;
    private String username;
}
