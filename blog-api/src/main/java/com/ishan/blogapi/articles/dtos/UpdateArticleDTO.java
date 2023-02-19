package com.ishan.blogapi.articles.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UpdateArticleDTO {
    private String title;
    private String subtitle;
    private String body;
    private List<String> tagList;
}
