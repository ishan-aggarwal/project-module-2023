package com.ishan.blogapi.articles.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleResponseDTO {
    private String title;
    private String subtitle;
    private String slug;
    private String body;
    private List<String> tagList;
    private String username;
    private String email;
}
