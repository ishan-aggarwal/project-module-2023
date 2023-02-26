package com.ishan.blogapi.articles.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateArticleDTO {
    private String subtitle;
    private String body;
    private List<String> tagList;
}
